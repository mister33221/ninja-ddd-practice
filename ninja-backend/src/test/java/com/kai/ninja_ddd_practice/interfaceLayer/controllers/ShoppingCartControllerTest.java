package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import com.kai.ninja_ddd_practice.applicationLayer.applicationService.ShoppingCartApplicationService;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateCartItemQuantityDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.principal.JwtUserPrincipal;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.AddToCartRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.CheckoutRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.UpdaateCartItemQuantityRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetShoppingCartResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("購物車控制器 - 完整單元測試")
class ShoppingCartControllerTest {

    @Mock
    private ShoppingCartApplicationService shoppingCartApplicationService;

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    private JwtUserPrincipal mockPrincipal;
    private UserId testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UserId.of(1L);
        mockPrincipal = JwtUserPrincipal.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .build();
    }

    @Test
    @DisplayName("添加商品到購物車 - 成功")
    void should_add_product_to_cart_successfully() {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
                .productId(1L)
                .quantity(2)
                .build();

        doNothing().when(shoppingCartApplicationService)
                .addProductToCart(any(UserId.class), any(AddToCartDto.class));

        // When
        assertDoesNotThrow(() -> 
            shoppingCartController.addProductToCart(mockPrincipal, request)
        );

        // Then
        verify(shoppingCartApplicationService).addProductToCart(
                eq(testUserId), 
                argThat(dto -> dto.getProductId().equals(1L) && dto.getQuantity().equals(2))
        );
    }

    @Test
    @DisplayName("獲取購物車 - 成功返回數據")
    void should_get_shopping_cart_successfully() {
        // Given
        GetShoppingCartDto mockDto = GetShoppingCartDto.builder()
                .shoppingCartId(1L)
                .userId(1L)
                .cartItems(new GetShoppingCartResponse.CartItem[0])
                .build();

        when(shoppingCartApplicationService.getShoppingCart(any(UserId.class)))
                .thenReturn(mockDto);

        // When
        GetShoppingCartResponse response = shoppingCartController.getShoppingCart(mockPrincipal);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getShoppingCartId());
        assertEquals(1L, response.getUserId());
        verify(shoppingCartApplicationService).getShoppingCart(testUserId);
    }

    @Test
    @DisplayName("更新購物車項目數量 - 成功")
    void should_update_cart_item_quantity_successfully() {
        // Given
        UpdaateCartItemQuantityRequest request = UpdaateCartItemQuantityRequest.builder()
                .productId(1L)
                .quantity(3)
                .build();

        doNothing().when(shoppingCartApplicationService)
                .updateCartItemQuantity(any(UserId.class), any(UpdateCartItemQuantityDto.class));

        // When
        assertDoesNotThrow(() -> 
            shoppingCartController.updateCartItemQuantity(mockPrincipal, request)
        );

        // Then
        verify(shoppingCartApplicationService).updateCartItemQuantity(
                eq(testUserId),
                argThat(dto -> dto.getProductId().equals(1L) && dto.getQuantity().equals(3))
        );
    }

    @Test
    @DisplayName("移除購物車項目 - 成功")
    void should_remove_cart_item_successfully() {
        // Given
        Long cartItemId = 1L;

        doNothing().when(shoppingCartApplicationService)
                .removeCartItem(any(UserId.class), anyLong());

        // When
        assertDoesNotThrow(() -> 
            shoppingCartController.removeCartItem(mockPrincipal, cartItemId)
        );

        // Then
        verify(shoppingCartApplicationService).removeCartItem(testUserId, cartItemId);
    }    @Test
    @DisplayName("結帳 - 成功")
    void should_checkout_successfully() {
        // Given
        CheckoutRequest request = CheckoutRequest.builder()
                .shoppingCartId(1L)
                .userId(1L)
                .build();

        doNothing().when(shoppingCartApplicationService)
                .checkout(any(UserId.class), any(CheckoutRequest.class));

        // When
        assertDoesNotThrow(() -> 
            shoppingCartController.checkout(mockPrincipal, request)
        );

        // Then
        verify(shoppingCartApplicationService).checkout(testUserId, request);
    }

    @Test
    @DisplayName("清空購物車 - 成功")
    void should_clear_cart_successfully() {
        // Given
        doNothing().when(shoppingCartApplicationService)
                .clearCart(any(UserId.class));

        // When
        assertDoesNotThrow(() -> 
            shoppingCartController.clearCart(mockPrincipal)
        );

        // Then
        verify(shoppingCartApplicationService).clearCart(testUserId);
    }

    @Test
    @DisplayName("Principal 轉換為 UserId - 驗證正確性")
    void should_convert_principal_to_userId_correctly() {
        // Given
        AddToCartRequest request = AddToCartRequest.builder()
                .productId(1L)
                .quantity(1)
                .build();

        doNothing().when(shoppingCartApplicationService)
                .addProductToCart(any(UserId.class), any(AddToCartDto.class));

        // When
        shoppingCartController.addProductToCart(mockPrincipal, request);

        // Then
        verify(shoppingCartApplicationService).addProductToCart(
                argThat(userId -> userId.getValue().equals(1L)),
                any(AddToCartDto.class)
        );
    }
}
