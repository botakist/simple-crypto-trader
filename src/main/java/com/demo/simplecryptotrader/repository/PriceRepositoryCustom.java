package com.demo.simplecryptotrader.repository;

import java.util.Map;

public interface PriceRepositoryCustom {
    Map<String, Object> getLatestRecord();
}
