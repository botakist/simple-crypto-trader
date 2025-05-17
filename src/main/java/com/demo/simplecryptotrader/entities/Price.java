package com.demo.simplecryptotrader.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
    private BigDecimal askPrice;
    private String source;
    private Timestamp timestamp;
}
