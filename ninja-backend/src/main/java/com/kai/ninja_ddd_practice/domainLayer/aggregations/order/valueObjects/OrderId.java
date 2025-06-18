package com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects;

import lombok.Value;

/**
 * 訂單 ID 值對象
 */
@Value
public class OrderId {
    Long value;
    
    public static OrderId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }
        return new OrderId(value);
    }
}
