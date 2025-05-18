package com.demo.simplecryptotrader.repository;

import java.util.List;
import java.util.Map;

public interface PriceRepositoryCustom {
    Map<String, Object> getLatestBestAggPrice(List<String> symbols);
}
