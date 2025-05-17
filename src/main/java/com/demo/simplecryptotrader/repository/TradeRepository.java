package com.demo.simplecryptotrader.repository;

import com.demo.simplecryptotrader.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
