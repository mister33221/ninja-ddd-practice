package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ProductCategoryEntity;
import org.springframework.stereotype.Component;

/**
 * 產品分類實體映射器
 */
@Component
public class ProductCategoryEntityMapper {

    /**
     * 將數據庫實體轉換為領域值對象
     */
    public ProductCategory toDomain(ProductCategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProductCategory.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .active(entity.getActive())
                .build();
    }

    /**
     * 將領域值對象轉換為數據庫實體
     */
    public ProductCategoryEntity toEntity(ProductCategory domain) {
        if (domain == null) {
            return null;
        }

        return ProductCategoryEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .active(domain.getActive())
                .build();
    }

    /**
     * 更新現有實體的數據
     */
    public void updateEntity(ProductCategoryEntity entity, ProductCategory domain) {
        if (entity == null || domain == null) {
            return;
        }

        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setActive(domain.getActive());
    }
}