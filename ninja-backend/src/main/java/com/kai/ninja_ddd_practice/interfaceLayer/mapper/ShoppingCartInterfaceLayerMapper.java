package com.kai.ninja_ddd_practice.interfaceLayer.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateCartItemQuantityDto;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.UpdaateCartItemQuantityRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetShoppingCartResponse;

public class ShoppingCartInterfaceLayerMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    private ShoppingCartInterfaceLayerMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static GetShoppingCartResponse convertGetShoppingCartDtoToResponse(GetShoppingCartDto getShoppingCartDto) {
        return objectMapper.convertValue(getShoppingCartDto, GetShoppingCartResponse.class);
    }

    public static UpdateCartItemQuantityDto convertUpdateCartItemQuantityRequestToDto(UpdaateCartItemQuantityRequest updaateCartItemQuantityRequest) {
        return objectMapper.convertValue(updaateCartItemQuantityRequest, UpdateCartItemQuantityDto.class);
    }
}
