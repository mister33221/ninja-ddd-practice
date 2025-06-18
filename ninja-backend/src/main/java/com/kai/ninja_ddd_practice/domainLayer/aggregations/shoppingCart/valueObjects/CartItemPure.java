package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

/**
 * 購物車項目值對象 - 純領域模型
 */
@Value
@Builder(toBuilder = true)
@Jacksonized
public class CartItemPure {
    
    CartItemId id;
    
    @NonNull
    ProductId productId;
    
    @NonNull
    String productName;
    
    String productImageUrl;
    
    int quantity;
    
    @NonNull
    BigDecimal unitPrice;

    /**
     * 增加數量
     */
    public CartItemPure incrementQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return this.toBuilder()
                .quantity(this.quantity + amount)
                .build();
    }

    /**
     * 計算總價
     */
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * 更新數量
     */
    public CartItemPure updateQuantity(int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        return this.toBuilder()
                .quantity(newQuantity)
                .build();
    }
}
