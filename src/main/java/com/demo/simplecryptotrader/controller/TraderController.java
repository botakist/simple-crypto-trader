package com.demo.simplecryptotrader.controller;


import com.demo.simplecryptotrader.service.TraderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class TraderController {

    private final TraderService traderService;

    @RequestMapping(path = "/getBestAggPrice", method = RequestMethod.GET)
    public ResponseEntity<Optional<Map<String, Object>>> getLatestBestAggPrice() {
        return ResponseEntity.ok(traderService.retrieveLatestBestPrice());
    }

    @RequestMapping(path = "/trade", method = RequestMethod.POST)
    public void tradeOnBestAggPrice() {

    }

    @RequestMapping(path = "/wallet", method = RequestMethod.GET)
    public void getWallet() {

    }

    @RequestMapping(path = "/history", method = RequestMethod.GET)
    public void getTradingHistory() {

    }
}
