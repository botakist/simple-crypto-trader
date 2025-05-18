package com.demo.simplecryptotrader.repository;

import com.demo.simplecryptotrader.model.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    Page<Trade> findAllByUserId(Long userId, Pageable pageable);
}
