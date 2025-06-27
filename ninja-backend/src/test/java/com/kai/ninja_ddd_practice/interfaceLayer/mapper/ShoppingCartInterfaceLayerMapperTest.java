package com.kai.ninja_ddd_practice.interfaceLayer.mapper;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateCartItemQuantityDto;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.AddToCartRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.UpdaateCartItemQuantityRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("購物車介面層Mapper - 單元測試")
class ShoppingCartInterfaceLayerMapperTest {

    @Test
    @DisplayName("轉換AddToCartRequest到DTO - 成功")
    void should_convert_add_to_cart_request_to_dto_successfully() {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
                .productId(1L)
                .quantity(3)
                .build();

        // When
        AddToCartDto dto = ProductInterfaceLayerMapper.convertAddToCartRequestToDto(request);

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getProductId());
        assertEquals(3, dto.getQuantity());
    }

    @Test
    @DisplayName("轉換AddToCartRequest到DTO - 處理null值")
    void should_handle_null_values_in_add_to_cart_request() {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
                .productId(null)
                .quantity(null)
                .build();

        // When
        AddToCartDto dto = ProductInterfaceLayerMapper.convertAddToCartRequestToDto(request);

        // Then
        assertNotNull(dto);
        assertNull(dto.getProductId());
        assertNull(dto.getQuantity());
    }

    @Test
    @DisplayName("轉換UpdateCartItemQuantityRequest到DTO - 成功")
    void should_convert_update_cart_item_quantity_request_to_dto_successfully() {
        // Given
        UpdaateCartItemQuantityRequest request = UpdaateCartItemQuantityRequest.builder()
                .productId(2L)
                .quantity(5)
                .build();

        // When
        UpdateCartItemQuantityDto dto = ShoppingCartInterfaceLayerMapper.convertUpdateCartItemQuantityRequestToDto(request);

        // Then
        assertNotNull(dto);
        assertEquals(2L, dto.getProductId());
        assertEquals(5, dto.getQuantity());
    }

    @Test
    @DisplayName("轉換UpdateCartItemQuantityRequest到DTO - 處理null值")
    void should_handle_null_values_in_update_cart_item_quantity_request() {
        // Given
        UpdaateCartItemQuantityRequest request = UpdaateCartItemQuantityRequest.builder()
                .productId(null)
                .quantity(null)
                .build();

        // When
        UpdateCartItemQuantityDto dto = ShoppingCartInterfaceLayerMapper.convertUpdateCartItemQuantityRequestToDto(request);

        // Then
        assertNotNull(dto);
        assertNull(dto.getProductId());
        assertNull(dto.getQuantity());
    }

    @Test
    @DisplayName("轉換AddToCartRequest到DTO - 邊界值測試")
    void should_handle_boundary_values_in_add_to_cart_request() {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
                .productId(Long.MAX_VALUE)
                .quantity(Integer.MAX_VALUE)
                .build();

        // When
        AddToCartDto dto = ProductInterfaceLayerMapper.convertAddToCartRequestToDto(request);

        // Then
        assertNotNull(dto);
        assertEquals(Long.MAX_VALUE, dto.getProductId());
        assertEquals(Integer.MAX_VALUE, dto.getQuantity());
    }

    @Test
    @DisplayName("轉換AddToCartRequest到DTO - 零值測試")
    void should_handle_zero_values_in_add_to_cart_request() {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
                .productId(0L)
                .quantity(0)
                .build();

        // When
        AddToCartDto dto = ProductInterfaceLayerMapper.convertAddToCartRequestToDto(request);

        // Then
        assertNotNull(dto);
        assertEquals(0L, dto.getProductId());
        assertEquals(0, dto.getQuantity());
    }
}
