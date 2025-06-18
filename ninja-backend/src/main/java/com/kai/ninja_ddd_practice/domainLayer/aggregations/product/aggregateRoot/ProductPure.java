package com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductDetails;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.Money;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.StockQuantity;
import lombok.*;

/**
 * 純淨的產品聚合根 - 不包含任何基礎設施依賴
 * 這是真正的領域模型，專注於業務邏輯
 */
@Getter
@AllArgsConstructor
@Builder
public class ProductPure {
    
    private final ProductId id;
    private ProductDetails details;
    private Money price;
    private StockQuantity stockQuantity;
    private ProductCategory category;
    private String status;
    private String imageUrl;

    // 業務邏輯方法
    public void updateStock(int quantity) {
        this.stockQuantity = this.stockQuantity.adjust(quantity);
    }

    public void updatePrice(Money newPrice) {
        if (newPrice.isNegativeOrZero()) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.price = newPrice;
    }
    
    public boolean isAvailable() {
        return "ACTIVE".equals(status) && stockQuantity.isAvailable();
    }
    
    public boolean hasEnoughStock(int requestedQuantity) {
        return stockQuantity.hasEnough(requestedQuantity);
    }
    
    public void reduceStock(int quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        updateStock(-quantity);
    }
    
    public void increaseStock(int quantity) {
        updateStock(quantity);
    }
}