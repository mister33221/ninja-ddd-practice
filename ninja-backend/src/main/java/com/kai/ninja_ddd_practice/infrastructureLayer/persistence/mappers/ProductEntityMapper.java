package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.*;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ProductEntity;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ProductCategoryEntity;
import org.springframework.stereotype.Component;

/**
 * 產品實體映射器 - 負責領域模型與數據庫實體之間的轉換
 * 這是防腐層的核心組件
 */
@Component
public class ProductEntityMapper {

    /**
     * 將數據庫實體轉換為領域模型
     */
    public ProductPure toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        ProductDetails details = ProductDetails.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .build();

        ProductCategory category = null;
        if (entity.getCategory() != null) {
            category = ProductCategory.builder()
                    .id(entity.getCategory().getId())
                    .name(entity.getCategory().getName())
                    .description(entity.getCategory().getDescription())
                    .active(entity.getCategory().getActive())
                    .build();
        }

        return ProductPure.builder()
                .id(ProductId.of(entity.getId()))
                .details(details)
                .price(Money.of(entity.getPrice(), entity.getCurrency()))
                .stockQuantity(StockQuantity.of(entity.getStockQuantity()))
                .category(category)
                .status(entity.getStatus())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    /**
     * 將領域模型轉換為數據庫實體
     */
    public ProductEntity toEntity(ProductPure domain) {
        if (domain == null) {
            return null;
        }

        ProductCategoryEntity categoryEntity = null;
        if (domain.getCategory() != null) {
            categoryEntity = ProductCategoryEntity.builder()
                    .id(domain.getCategory().getId())
                    .name(domain.getCategory().getName())
                    .description(domain.getCategory().getDescription())
                    .active(domain.getCategory().getActive())
                    .build();
        }

        return ProductEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .name(domain.getDetails().getName())
                .description(domain.getDetails().getDescription())
                .price(domain.getPrice().getAmount())
                .currency(domain.getPrice().getCurrency())
                .stockQuantity(domain.getStockQuantity().getValue())
                .category(categoryEntity)
                .status(domain.getStatus())
                .imageUrl(domain.getImageUrl())
                .build();
    }

    /**
     * 更新現有實體的數據（用於更新操作）
     */
    public void updateEntity(ProductEntity entity, ProductPure domain) {
        if (entity == null || domain == null) {
            return;
        }

        entity.setName(domain.getDetails().getName());
        entity.setDescription(domain.getDetails().getDescription());
        entity.setPrice(domain.getPrice().getAmount());
        entity.setCurrency(domain.getPrice().getCurrency());
        entity.setStockQuantity(domain.getStockQuantity().getValue());
        entity.setStatus(domain.getStatus());
        entity.setImageUrl(domain.getImageUrl());

        if (domain.getCategory() != null) {
            ProductCategoryEntity categoryEntity = ProductCategoryEntity.builder()
                    .id(domain.getCategory().getId())
                    .name(domain.getCategory().getName())
                    .description(domain.getCategory().getDescription())
                    .active(domain.getCategory().getActive())
                    .build();
            entity.setCategory(categoryEntity);
        }
    }
}