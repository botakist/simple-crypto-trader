package com.demo.simplecryptotrader.repository.impl;

import com.demo.simplecryptotrader.model.Price;
import com.demo.simplecryptotrader.repository.PriceRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PriceRepositoryImpl implements PriceRepositoryCustom {
    private static final List<String> TARGET_PAIRS = List.of("BTCUSDT", "ETHUSDT");
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<String, Object> getLatestRecord() {
        Map<String, Object> result = new HashMap<>();

        for (String pair : TARGET_PAIRS) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = cb.createTupleQuery();
            Root<Price> root = query.from(Price.class);

            query.multiselect(
                    root.get("bidPrice").alias("bidPrice"),
                    root.get("bidSource").alias("bidSource"),
                    root.get("askPrice").alias("askPrice"),
                    root.get("askSource").alias("askSource"),
                    root.get("timestamp").alias("timestamp")
            );

            query.where(cb.equal(root.get("pair"), pair));
            query.orderBy(cb.desc(root.get("timestamp")));

            List<Tuple> tupleList = entityManager.createQuery(query)
                    .setMaxResults(1)
                    .getResultList();

            if (!tupleList.isEmpty()) {
                Tuple t = tupleList.get(0);
                Map<String, Object> data = new HashMap<>();
                data.put("bidPrice", t.get("bidPrice"));
                data.put("bidSource", t.get("bidSource"));
                data.put("askPrice", t.get("askPrice"));
                data.put("askSource", t.get("askSource"));
                data.put("timestamp", t.get("timestamp"));
                result.put(pair, data);
            }
        }
        return result;
    }

}


