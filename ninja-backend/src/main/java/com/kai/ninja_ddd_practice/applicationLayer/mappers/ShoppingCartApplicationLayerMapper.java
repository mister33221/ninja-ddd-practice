package com.kai.ninja_ddd_practice.applicationLayer.mappers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCart;

/**
 * 在 Application 層中的 Mapper 類別，
 * 負責
 * 1. 將 Domain 層的 Aggregate 轉換為 Application 層的 Dto。
 * 2. 將 Application 層的 Dto 轉換為 Domain 層的 Aggregate。
 */
public class ShoppingCartApplicationLayerMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    private ShoppingCartApplicationLayerMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static GetShoppingCartDto covertShoppingCartToGetShoppingCartDto(ShoppingCart object) {

        return objectMapper.convertValue(object, GetShoppingCartDto.class);

    }
}
