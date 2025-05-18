package com.demo.simplecryptotrader.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WalletBalanceResponse {
    private String currency;
    private BigDecimal balance;
}
