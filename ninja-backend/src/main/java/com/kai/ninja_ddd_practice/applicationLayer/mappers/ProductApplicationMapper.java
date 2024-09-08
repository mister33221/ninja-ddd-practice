package com.kai.ninja_ddd_practice.applicationLayer.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetProductCardsDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.AddToCartRequest;

public class ProductApplicationMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private ProductApplicationMapper() {
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

    public static AddToCartDto convertAddToCartRequestToDto(AddToCartRequest addToCartRequest) {
        return objectMapper.convertValue(addToCartRequest, AddToCartDto.class);
    }
    
}
