package com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects;

import lombok.Value;

/**
 * 訂單項目 ID 值對象
 */
@Value
public class OrderItemId {
    Long value;
    
    public static OrderItemId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Order item ID must be positive");
        }
        return new OrderItemId(value);
    }
}
