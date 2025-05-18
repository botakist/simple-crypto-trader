package com.demo.simplecryptotrader.service;

import com.demo.simplecryptotrader.model.Trade;
import com.demo.simplecryptotrader.model.TradeRequest;
import com.demo.simplecryptotrader.model.WalletBalanceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TraderService {

    void retrievePrice();
    Map<String, Object> retrieveLatestBestPrice(List<String> symbols);
    ResponseEntity<Object> buyWithLatestBestAggPrice(TradeRequest tradeRequest);
    WalletBalanceResponse retrieveWalletBalance(Long userId);
    PagedModel<EntityModel<Trade>> retrieveTrades(Long userId, Pageable pageable);
}
