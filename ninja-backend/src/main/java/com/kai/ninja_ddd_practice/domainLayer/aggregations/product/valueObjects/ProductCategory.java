package com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects;

import lombok.*;

/**
 * 產品分類值對象 - 純淨的領域模型
 * 不包含任何基礎設施依賴
 */
@Value
@Builder
public class ProductCategory {
    Long id;
    String name;
    String description;
    Boolean active;

    /**
     * 檢查分類是否為活躍狀態
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

    /**
     * 創建新的活躍分類
     */
    public static ProductCategory createActive(String name, String description) {
        return ProductCategory.builder()
                .name(name)
                .description(description)
                .active(true)
                .build();
    }

    /**
     * 停用分類
     */
    public ProductCategory deactivate() {
        return ProductCategory.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .active(false)
                .build();
    }

    /**
     * 啟用分類
     */
    public ProductCategory activate() {
        return ProductCategory.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .active(true)
                .build();
    }
}
