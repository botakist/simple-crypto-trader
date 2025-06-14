package com.demo.simplecryptotrader.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "prices")
public class Price {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String pair;
    private BigDecimal bidPrice;
    private BigDecimal bidQty;
    private String bidSource;
    private BigDecimal askPrice;
    private BigDecimal askQty;
    private String askSource;
    private Instant timestamp;
}
