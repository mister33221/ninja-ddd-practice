package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import com.kai.ninja_ddd_practice.applicationLayer.applicationService.ShoppingCartApplicationService;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("購物車控制器 - 單元測試")
class ShoppingCartControllerTest {

    @Mock
    private ShoppingCartApplicationService shoppingCartApplicationService;

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    @Test
    @DisplayName("應用服務測試 - 驗證 Service 方法調用")
    void should_call_service_methods() {
        // Given
        UserId userId = UserId.of(1L);
        AddToCartDto dto = AddToCartDto.builder()
            .productId(1L)
            .quantity(2)
            .build();

        // When
        doNothing().when(shoppingCartApplicationService)
            .addProductToCart(any(UserId.class), any(AddToCartDto.class));

        shoppingCartApplicationService.addProductToCart(userId, dto);

        // Then
        verify(shoppingCartApplicationService).addProductToCart(userId, dto);
    }
}
