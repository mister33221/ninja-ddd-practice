package com.kai.ninja_ddd_practice.applicationLayer.mappers;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetProductCardsDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;

/**
 * 在 Application 層中的 Mapper 類別，
 * 負責
 * 1. 將 Domain 層的 Aggregate 轉換為 Application 層的 Dto。
 * 2. 將 Application 層的 Dto 轉換為 Domain 層的 Aggregate。
 */
public class ProductApplicationLayerMapper {
    
    private ProductApplicationLayerMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }public static GetProductCardsDto covertProductToGetProductCardsDto(ProductPure product) {
        return GetProductCardsDto.builder()
                .id(product.getId().getValue())
                .name(product.getDetails().getName())
                .description(product.getDetails().getDescription())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice().getAmount())
                .category(product.getCategory().getName())
                .build();
    }


    
}
