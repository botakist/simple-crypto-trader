package com.demo.simplecryptotrader.service.impl;

import com.demo.simplecryptotrader.model.BinanceTicker;
import com.demo.simplecryptotrader.model.HuobiResponse;
import com.demo.simplecryptotrader.model.HuobiTicker;
import com.demo.simplecryptotrader.model.Price;
import com.demo.simplecryptotrader.repository.PriceRepository;
import com.demo.simplecryptotrader.repository.TradeRepository;
import com.demo.simplecryptotrader.repository.UserRepository;
import com.demo.simplecryptotrader.repository.WalletRepository;
import com.demo.simplecryptotrader.service.TraderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraderServiceImpl implements TraderService {

    private final PriceRepository priceRepository;
    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WebClient webClient;

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
                            p.setBidSource(SOURCE_HUOBI);
                            p.setAskPrice(huobiTicker.getAsk());
                            p.setAskSource(SOURCE_HUOBI);
                            p.setTimestamp(LocalDateTime.now());
                            priceRepository.save(p);
                            continue;
                        }

                        if (huobiTicker == null) {
                            // set best and ask price to binance
                            Price p = new Price();
                            p.setPair(pair);
                            p.setBidPrice(binanceTicker.getBidPrice());
                            p.setBidSource(SOURCE_BINANCE);
                            p.setAskPrice(binanceTicker.getAskPrice());
                            p.setAskSource(SOURCE_BINANCE);
                            p.setTimestamp(LocalDateTime.now());
                            priceRepository.save(p);
                            continue;
                        }

                        // both exists, need to compare and get best bid and ask price

                        BigDecimal bidPrice;
                        String bidSource;
                        if (binanceTicker.getBidPrice().compareTo(huobiTicker.getBid()) >= 0) {
                            bidPrice = binanceTicker.getBidPrice();
                            bidSource = SOURCE_BINANCE;
                        } else {
                            bidPrice = huobiTicker.getBid();
                            bidSource = SOURCE_HUOBI;
                        }

                        BigDecimal askPrice;
                        String askSource;
                        if (binanceTicker.getAskPrice().compareTo(huobiTicker.getAsk()) <= 0) {
                            askPrice = binanceTicker.getAskPrice();
                            askSource = SOURCE_BINANCE;
                        } else {
                            askPrice = huobiTicker.getAsk();
                            askSource = SOURCE_HUOBI;
                        }

                        log.info("[{}] Best Bid: {} ({})", pair, bidPrice, bidSource);
                        log.info("[{}] Best Ask: {} ({})", pair, askPrice, askSource);

                        Price p = new Price();
                        p.setPair(pair);
                        p.setBidPrice(bidPrice);
                        p.setBidSource(bidSource);
                        p.setAskPrice(askPrice);
                        p.setAskSource(askSource);
                        p.setTimestamp(LocalDateTime.now());
                        priceRepository.save(p);
                    }
                })
                .doOnError(error -> log.error("Error: ", error))
                .subscribe();
    }

    @Override
    public Optional<Map<String, Object>> retrieveLatestBestPrice() {
        return Optional.ofNullable(priceRepository.getLatestRecord());
    }
}
