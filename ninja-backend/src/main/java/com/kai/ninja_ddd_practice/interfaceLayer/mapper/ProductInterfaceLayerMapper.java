package com.kai.ninja_ddd_practice.interfaceLayer.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateCartItemQuantityDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.AddToCartRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.UpdaateCartItemQuantityRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetProductsResponse;

import java.util.List;

/**
 * 在 Interface 層中的 Mapper 類別，
 * 負責
 * 1. 將 Request object 轉換為 Application 層的 Dto。
 * 2. 將 Application 層的 Dto 轉換為 Response object。
 */
public class ProductInterfaceLayerMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ProductInterfaceLayerMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static AddToCartDto convertAddToCartRequestToDto(AddToCartRequest addToCartRequest) {
        return objectMapper.convertValue(addToCartRequest, AddToCartDto.class);
    }

}
