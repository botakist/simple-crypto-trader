package com.demo.simplecryptotrader.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "trades")
public class Trade {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long userId;
    private String pair;
    private String type;
    private BigDecimal amt;
    private BigDecimal price;
    private BigDecimal total;
    private Timestamp timestamp;
}
