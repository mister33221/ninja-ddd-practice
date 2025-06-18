package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects;

import lombok.Value;

/**
 * 購物車 ID 值對象
 */
@Value
public class ShoppingCartId {
    Long value;
    
    public static ShoppingCartId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Shopping cart ID must be positive");
        }
        return new ShoppingCartId(value);
    }
}
