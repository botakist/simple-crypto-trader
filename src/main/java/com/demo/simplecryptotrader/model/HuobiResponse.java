package com.demo.simplecryptotrader.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HuobiResponse {
    private List<HuobiTicker> data;
    private String status;
    private Long ts;
}
