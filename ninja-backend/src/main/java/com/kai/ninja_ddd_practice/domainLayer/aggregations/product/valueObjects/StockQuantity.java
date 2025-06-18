package com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects;

import lombok.Value;

/**
 * 庫存數量值對象
 */
@Value
public class StockQuantity {
    int value;
    
    public StockQuantity(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.value = value;
    }
    
    public static StockQuantity of(int value) {
        return new StockQuantity(value);
    }
    
    public boolean isAvailable() {
        return value > 0;
    }
    
    public boolean hasEnough(int requestedQuantity) {
        return value >= requestedQuantity;
    }
    
    public StockQuantity adjust(int adjustment) {
        int newValue = value + adjustment;
        if (newValue < 0) {
            throw new IllegalArgumentException("Insufficient stock. Current: " + value + ", Requested: " + Math.abs(adjustment));
        }
        return new StockQuantity(newValue);
    }
}