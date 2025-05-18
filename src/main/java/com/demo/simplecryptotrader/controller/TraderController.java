package com.demo.simplecryptotrader.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TraderController {

    @RequestMapping(path = "/getBestAggPrice", method = RequestMethod.GET)
    public void getBestAggPrice() {

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
