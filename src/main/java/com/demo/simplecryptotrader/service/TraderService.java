package com.demo.simplecryptotrader.service;

import java.util.Map;
import java.util.Optional;

public interface TraderService {

    void retrievePrice();
    Optional<Map<String, Object>> retrieveLatestBestPrice();
}
