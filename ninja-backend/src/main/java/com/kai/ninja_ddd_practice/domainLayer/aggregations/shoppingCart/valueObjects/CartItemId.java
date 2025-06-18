package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects;

import lombok.Value;

/**
 * 購物車項目 ID 值對象
 */
@Value
public class CartItemId {
    Long value;
    
    public static CartItemId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Cart item ID must be positive");
        }
        return new CartItemId(value);
    }
}
