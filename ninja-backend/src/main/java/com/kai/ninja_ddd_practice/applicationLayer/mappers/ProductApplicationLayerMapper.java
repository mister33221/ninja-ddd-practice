package com.kai.ninja_ddd_practice.applicationLayer.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetProductCardsDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.AddToCartRequest;

/**
 * 在 Application 層中的 Mapper 類別，
 * 負責
 * 1. 將 Domain 層的 Aggregate 轉換為 Application 層的 Dto。
 * 2. 將 Application 層的 Dto 轉換為 Domain 層的 Aggregate。
 */
public class ProductApplicationLayerMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private ProductApplicationLayerMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    public static GetProductCardsDto covertProductToGetProductCardsDto(Product product) {
        // 創建一個ObjectNode來構建JSON對象
        ObjectNode jsonNode = objectMapper.createObjectNode();

        // 設置DTO的屬性
        jsonNode.put("id", product.getId());
        jsonNode.put("name", product.getDetails().getName());
        jsonNode.put("description", product.getDetails().getDescription());
        jsonNode.put("imageUrl", product.getImageUrl());
        jsonNode.put("price", product.getPrice().toString());
        jsonNode.put("category", product.getCategory().getName());

        // 將JSON對象轉換為DTO
        return objectMapper.convertValue(jsonNode, GetProductCardsDto.class);
    }


    
}
