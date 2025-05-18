package com.demo.simplecryptotrader.controller;


import com.demo.simplecryptotrader.service.TraderService;
import lombok.AllArgsConstructor;
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
    public void getWallet() {

    }

    @RequestMapping(path = "/history", method = RequestMethod.GET)
    public void getTradingHistory() {

    }
}
