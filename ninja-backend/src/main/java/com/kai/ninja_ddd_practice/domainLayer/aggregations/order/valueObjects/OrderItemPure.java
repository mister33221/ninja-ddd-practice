package com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

/**
 * 訂單項目值對象 - 純領域模型
 */
@Value
@Builder(toBuilder = true)
@Jacksonized
public class OrderItemPure {
    
    OrderItemId id;
    
    @NonNull
    ProductId productId;
    
    @NonNull
    String productName;
    
    int quantity;
    
    @NonNull
    BigDecimal unitPrice;

    /**
     * 計算總價
     */
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * 更新數量
     */
    public OrderItemPure updateQuantity(int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        return this.toBuilder()
                .quantity(newQuantity)
                .build();
    }
}
