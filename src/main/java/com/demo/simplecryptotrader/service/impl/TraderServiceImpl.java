package com.demo.simplecryptotrader.service.impl;

import com.demo.simplecryptotrader.exception.InsufficientWalletBalanceException;
import com.demo.simplecryptotrader.exception.UserIdDoesNotExistException;
import com.demo.simplecryptotrader.model.*;
import com.demo.simplecryptotrader.repository.PriceRepository;
import com.demo.simplecryptotrader.repository.TradeRepository;
import com.demo.simplecryptotrader.repository.WalletRepository;
import com.demo.simplecryptotrader.service.TraderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraderServiceImpl implements TraderService {

    private final PriceRepository priceRepository;
    private final TradeRepository tradeRepository;
    private final WalletRepository walletRepository;
    private final WebClient webClient;

    private final PagedResourcesAssembler<Trade> pagedResourcesAssembler;

    @Value("${binance.ticker.url}")
    private String binanceTickerUrl;

    @Value("${huobi.ticker.url}")
    private String huobiTickerUrl;

    private static final List<String> TARGET_PAIRS = List.of("BTCUSDT", "ETHUSDT");
    private static final String SOURCE_BINANCE = "Binance";
    private static final String SOURCE_HUOBI = "Huobi";


    @Scheduled(fixedRate = 10000)
    @Override
    public void retrievePrice() {
        Mono<List<BinanceTicker>> binanceMono = webClient.get()
                .uri(binanceTickerUrl)
                .retrieve()
                .bodyToFlux(BinanceTicker.class)
                .collectList();

        Mono<HuobiResponse> huobiMono = webClient.get()
                .uri(huobiTickerUrl)
                .retrieve()
                .bodyToMono(HuobiResponse.class);

        Mono.zip(binanceMono, huobiMono)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(tuple -> {
                    Map<String, BinanceTicker> binanceTickerMap = tuple.getT1().stream()
                            .filter(t -> TARGET_PAIRS.contains(t.getSymbol()))
                            .collect(Collectors.toMap(BinanceTicker::getSymbol, Function.identity()));

                    Map<String, HuobiTicker> huobiTickerMap = tuple.getT2().getData().stream()
                            .filter(t -> TARGET_PAIRS.contains(t.getSymbol().toUpperCase()))
                            .collect(Collectors.toMap(t -> t.getSymbol().toUpperCase(), Function.identity()));

                    for (String pair : TARGET_PAIRS) {
                        BinanceTicker binanceTicker = binanceTickerMap.get(pair);
                        HuobiTicker huobiTicker = huobiTickerMap.get(pair);

                        if (binanceTicker == null && huobiTicker == null) {
                            continue;
                        }

                        if (binanceTicker == null) {
                            // set best and ask price to huobi
                            Price p = new Price();
                            p.setPair(pair);
                            p.setBidPrice(huobiTicker.getBid());
                            p.setBidQty(huobiTicker.getBidSize());
                            p.setBidSource(SOURCE_HUOBI);
                            p.setAskPrice(huobiTicker.getAsk());
                            p.setAskQty(huobiTicker.getAskSize());
                            p.setAskSource(SOURCE_HUOBI);
                            p.setTimestamp(Instant.now());
                            priceRepository.save(p);
                            continue;
                        }

                        if (huobiTicker == null) {
                            // set best and ask price to binance
                            Price p = new Price();
                            p.setPair(pair);
                            p.setBidPrice(binanceTicker.getBidPrice());
                            p.setBidQty(binanceTicker.getBidQty());
                            p.setBidSource(SOURCE_BINANCE);
                            p.setAskPrice(binanceTicker.getAskPrice());
                            p.setAskQty(binanceTicker.getAskQty());
                            p.setAskSource(SOURCE_BINANCE);
                            p.setTimestamp(Instant.now());
                            priceRepository.save(p);
                            continue;
                        }

                        // both exists, need to compare and get best bid and ask price

                        BigDecimal bidPrice;
                        BigDecimal bidQty;
                        String bidSource;
                        if (binanceTicker.getBidPrice().compareTo(huobiTicker.getBid()) >= 0) {
                            bidPrice = binanceTicker.getBidPrice();
                            bidQty = binanceTicker.getBidQty();
                            bidSource = SOURCE_BINANCE;
                        } else {
                            bidPrice = huobiTicker.getBid();
                            bidQty = huobiTicker.getBidSize();
                            bidSource = SOURCE_HUOBI;
                        }

                        BigDecimal askPrice;
                        BigDecimal askQty;
                        String askSource;
                        if (binanceTicker.getAskPrice().compareTo(huobiTicker.getAsk()) <= 0) {
                            askPrice = binanceTicker.getAskPrice();
                            askQty = binanceTicker.getAskQty();
                            askSource = SOURCE_BINANCE;
                        } else {
                            askPrice = huobiTicker.getAsk();
                            askQty = huobiTicker.getAskSize();
                            askSource = SOURCE_HUOBI;
                        }

                        log.info("[{}] Best Bid: {} ({})", pair, bidPrice, bidSource);
                        log.info("[{}] Best Ask: {} ({})", pair, askPrice, askSource);

                        Price p = new Price();
                        p.setPair(pair);
                        p.setBidPrice(bidPrice);
                        p.setBidQty(bidQty);
                        p.setBidSource(bidSource);
                        p.setAskPrice(askPrice);
                        p.setAskQty(askQty);
                        p.setAskSource(askSource);
                        p.setTimestamp(Instant.now());
                        priceRepository.save(p);
                    }
                })
                .doOnError(error -> log.error("Error: ", error))
                .subscribe();
    }

    @Override
    public Map<String, Object> retrieveLatestBestPrice(List<String> symbols) {
        return priceRepository.getLatestBestAggPrice(symbols);
    }

    @Transactional
    @Override
    public ResponseEntity<Object> buyWithLatestBestAggPrice(TradeRequest tradeRequest) {
        // check wallet ensure enough balance
        Wallet currentUserWallet = walletRepository.findByUserId(tradeRequest.getUserId());
        if (currentUserWallet == null) {
            String errMessage = MessageFormat.format("user id {0} does not exist", tradeRequest.getUserId());
            log.info(errMessage);
            throw new UserIdDoesNotExistException(errMessage);
        }
        if (currentUserWallet.getBalance().subtract(tradeRequest.getPrice()).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientWalletBalanceException("insufficient wallet balance.");
        }

        // retrieve latest ask price for the specific pair (BTCUSDT or ETHUSDT)
        Map<String, Object> result = priceRepository.getLatestBestAggPrice(Arrays.asList(tradeRequest.getSymbol()));
        Map<String, Object> symbolData = (Map<String, Object>) result.get(tradeRequest.getSymbol());

        BigDecimal exchangeAskPrice = (BigDecimal) symbolData.get("askPrice");
        BigDecimal exchangeAskSize = (BigDecimal) symbolData.get("askQty");
        String exchange = (String) symbolData.get("askSource");

        // set rounding down based on Binance's asset precision
        BigDecimal calculatedAskSizeBasedOnExchangeAskPrice = tradeRequest.getPrice().divide(exchangeAskPrice, 8, RoundingMode.DOWN);

        BigDecimal maxAskSize = calculatedAskSizeBasedOnExchangeAskPrice;
        if (calculatedAskSizeBasedOnExchangeAskPrice.compareTo(exchangeAskSize) > 0) {
            maxAskSize = exchangeAskSize; // maximum permitted ask size from exchange
        }

        Trade t = new Trade();
        t.setUserId(tradeRequest.getUserId());
        t.setSymbol(tradeRequest.getSymbol());
        t.setSide("BUY");
        t.setQty(maxAskSize);
        t.setPrice(exchangeAskPrice); // price at which the trade occurred
        t.setExchange(exchange);
        t.setTimestamp(Instant.now());
        tradeRepository.save(t);

        // deduct balance for wallet
        currentUserWallet.setBalance(currentUserWallet.getBalance().subtract(tradeRequest.getPrice()));
        walletRepository.save(currentUserWallet);

        Map<String, Object> response = Map.of(
                "status", HttpStatus.OK.value(),
                "message", "Trade performed successfully",
                "timestamp", Instant.now().toString()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public WalletBalanceResponse retrieveWalletBalance(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet == null) {
            String errMessage = MessageFormat.format("user id {0} does not exist", userId);
            log.info(errMessage);
            throw new UserIdDoesNotExistException(errMessage);
        }
        WalletBalanceResponse response = new WalletBalanceResponse();
        response.setCurrency(wallet.getCurrency());
        response.setBalance(wallet.getBalance());
        return response;
    }

    @Override
    public PagedModel<EntityModel<Trade>> retrieveTrades(Long userId, Pageable pageable) {
        Page<Trade> tradesPage = tradeRepository.findAllByUserId(userId, pageable);
        return pagedResourcesAssembler.toModel(tradesPage);
    }
}
