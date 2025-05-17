package com.demo.simplecryptotrader.controller;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TraderController {
    @Scheduled(fixedRate = 10000)
    public void retrievePricing() {

    }

    @RequestMapping(method = RequestMethod.GET)
    public void getBestAggPrice() {

    }

    @RequestMapping(method = RequestMethod.POST)
    public void tradeOnBestAggPrice() {

    }

    @RequestMapping(method = RequestMethod.GET)
    public void getWallet() {

    }

    @RequestMapping(method = RequestMethod.GET)
    public void getTradingHistory() {

    }
}
