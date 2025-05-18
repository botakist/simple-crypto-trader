package com.demo.simplecryptotrader.service;

import com.demo.simplecryptotrader.model.Trade;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TraderService {

    void retrievePrice();
    Map<String, Object> retrieveLatestBestPrice(List<String> pairs);
    ResponseEntity<Object> buyWithLatestBestAggPrice(String symbol, Long userId, BigDecimal price);
    PagedModel<EntityModel<Trade>> retrieveTrades(Long userId, Pageable pageable);
}
