package com.demo.simplecryptotrader.repository;

import com.demo.simplecryptotrader.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long>, PriceRepositoryCustom {
}
