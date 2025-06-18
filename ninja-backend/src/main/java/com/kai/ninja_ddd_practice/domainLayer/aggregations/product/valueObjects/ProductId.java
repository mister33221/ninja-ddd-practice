package com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects;

import lombok.Value;

/**
 * 產品ID值對象
 */
@Value
public class ProductId {
    Long value;
    
    public ProductId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
        this.value = value;
    }
    
    public static ProductId of(Long value) {
        return new ProductId(value);
    }
}