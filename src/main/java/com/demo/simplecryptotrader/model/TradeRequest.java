package com.demo.simplecryptotrader.model;

import com.demo.simplecryptotrader.validators.AllowedSymbols;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TradeRequest {

    @AllowedSymbols
    private String symbol;
    @NotNull
    @Positive(message = "invalid user id")
    private Long userId;
    @NotNull(message = "price cannot be empty")
    @Positive(message = "price cannot be negative or zero")
    private BigDecimal price;
}
