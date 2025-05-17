package com.demo.simplecryptotrader.service;

import org.springframework.beans.factory.annotation.Value;

public class TraderService {

    @Value("${binance.ticker.url}")
    private String binanceTickerUrl;

    @Value("${huobi.ticker.url}")
    private String huobiTickerUrl;



}
