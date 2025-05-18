package com.demo.simplecryptotrader.controller;


import com.demo.simplecryptotrader.model.Trade;
import com.demo.simplecryptotrader.model.TradeRequest;
import com.demo.simplecryptotrader.model.WalletBalanceResponse;
import com.demo.simplecryptotrader.service.TraderService;
import com.demo.simplecryptotrader.validators.AllowedSymbols;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Validated
public class TraderController {
    private static final List<String> TARGET_PAIRS = List.of("BTCUSDT", "ETHUSDT");
    private final TraderService traderService;

    @RequestMapping(path = "/getBestAggPrice", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getLatestBestAggPrice(
            @RequestParam(required = false) @AllowedSymbols List<String> symbols) {
        if (symbols == null || symbols.isEmpty()) {
            symbols = TARGET_PAIRS;
        }
        return ResponseEntity.ok(traderService.retrieveLatestBestPrice(symbols));
    }

    @RequestMapping(path = "/trade", method = RequestMethod.POST)
    public ResponseEntity<Object> tradeOnBestAggPrice(@RequestBody @Valid TradeRequest tradeRequest) {
        return traderService.buyWithLatestBestAggPrice(tradeRequest);
    }

    @RequestMapping(path = "/wallet/{userId}", method = RequestMethod.GET)
    public ResponseEntity<WalletBalanceResponse> getWallet(
            @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(traderService.retrieveWalletBalance(userId));
    }

    @RequestMapping(path = "/history/{userId}", method = RequestMethod.GET)
    public ResponseEntity<PagedModel<EntityModel<Trade>>> getTradingHistory(
            @PathVariable(name = "userId") Long userId,
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(traderService.retrieveTrades(userId, pageable));
    }
}
