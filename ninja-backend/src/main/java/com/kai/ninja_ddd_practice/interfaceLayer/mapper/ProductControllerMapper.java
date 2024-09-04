package com.kai.ninja_ddd_practice.interfaceLayer.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetProductsResponse;

import java.util.List;


public class ProductControllerMapper {

    private final ObjectMapper objectMapper;

    public ProductControllerMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GetProductsResponse toGetProductsResponse(List<Product> productList) {
        return null;
    }
}
