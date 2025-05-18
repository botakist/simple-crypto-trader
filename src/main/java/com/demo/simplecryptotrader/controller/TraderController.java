package com.demo.simplecryptotrader.controller;


import com.demo.simplecryptotrader.model.Trade;
import com.demo.simplecryptotrader.model.Wallet;
import com.demo.simplecryptotrader.repository.TradeRepository;
import com.demo.simplecryptotrader.repository.WalletRepository;
import com.demo.simplecryptotrader.service.TraderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class TraderController {
    private static final List<String> TARGET_PAIRS = List.of("BTCUSDT", "ETHUSDT");
    private final TraderService traderService;
    private final WalletRepository walletRepository;
    private final TradeRepository tradeRepository;

    @RequestMapping(path = "/getBestAggPrice", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getLatestBestAggPrice(@RequestParam(required = false) List<String> pairs) {
        if (pairs == null || pairs.isEmpty()) {
            pairs = TARGET_PAIRS;
        }
        return ResponseEntity.ok(traderService.retrieveLatestBestPrice(pairs));
    }

    @RequestMapping(path = "/trade", method = RequestMethod.POST)
    public ResponseEntity<Object> tradeOnBestAggPrice(
            @RequestParam() String symbol,
            @RequestParam() Long userId,
            @RequestParam() BigDecimal price) {
        return traderService.buyWithLatestBestAggPrice(symbol, userId, price);
    }

    @RequestMapping(path = "/wallet", method = RequestMethod.GET)
    public ResponseEntity<Wallet> getWallet(@RequestParam() String userId) {
        return ResponseEntity.ok(walletRepository.findByUserId(Long.valueOf(userId)));
    }

    @RequestMapping(path = "/history/{userId}", method = RequestMethod.GET)
    public ResponseEntity<PagedModel<EntityModel<Trade>>> getTradingHistory(
            @PathVariable("userId") Long userId,
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(traderService.retrieveTrades(userId, pageable));
    }
}
