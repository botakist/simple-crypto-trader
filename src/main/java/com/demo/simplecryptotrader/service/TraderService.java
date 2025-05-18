package com.demo.simplecryptotrader.service;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TraderService {

    void retrievePrice();
    Map<String, Object> retrieveLatestBestPrice(List<String> pairs);
    ResponseEntity<Object> buyWithLatestBestAggPrice(String symbol, Long userId, BigDecimal price);
}
