package com.demo.simplecryptotrader.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class SymbolData {
    private BigDecimal bidPrice;
    private BigDecimal bidQty;
    private String bidSource;
    private BigDecimal askPrice;
    private BigDecimal askQty;
    private String askSource;
    private Instant timestamp;
}
