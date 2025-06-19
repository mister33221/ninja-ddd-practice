package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import com.kai.ninja_ddd_practice.applicationLayer.applicationService.ShoppingCartApplicationService;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateCartItemQuantityDto;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.AddToCartRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.CheckoutRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.UpdaateCartItemQuantityRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetShoppingCartResponse;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
@ContextConfiguration(classes = {ShoppingCartController.class, ShoppingCartControllerTest.TestConfig.class})
@DisplayName("購物車控制器 - Web API 測試")
class ShoppingCartControllerTest {

    @Configuration
    static class TestConfig implements WebMvcConfigurer {
        // 測試配置，不註冊任何攔截器，覆蓋生產環境的 WebConfig
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartApplicationService shoppingCartApplicationService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String validToken;
    private AddToCartRequest addToCartRequest;
    private UpdaateCartItemQuantityRequest updateRequest;
    private CheckoutRequest checkoutRequest;    @BeforeEach
    void setUp() {
        validToken = "Bearer valid.jwt.token";
        
        addToCartRequest = AddToCartRequest.builder()
            .productId(1L)
            .quantity(2)
            .build();
        
        updateRequest = UpdaateCartItemQuantityRequest.builder()
            .id(1L)
            .quantity(3)
            .build();
        
        checkoutRequest = CheckoutRequest.builder()
            .shoppingCartId(1L)
            .userId(1L)
            .cartItems(Collections.emptyList())
            .build();
    }

    @Test
    @DisplayName("POST /shopping-cart/add-to-cart - 成功添加商品到購物車")
    void should_add_product_to_cart_successfully() throws Exception {
        // Given
        doNothing().when(shoppingCartApplicationService).addProductToCart(anyString(), any(AddToCartDto.class));

        // When & Then
        mockMvc.perform(post("/shopping-cart/add-to-cart")
                .header("Authorization", validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addToCartRequest)))
                .andExpect(status().isOk());

        verify(shoppingCartApplicationService).addProductToCart(eq(validToken), any(AddToCartDto.class));
    }

    @Test
    @DisplayName("POST /shopping-cart/add-to-cart - 缺少 Authorization header 應該返回 400")
    void should_return_bad_request_when_missing_authorization_header() throws Exception {
        // When & Then
        mockMvc.perform(post("/shopping-cart/add-to-cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addToCartRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(shoppingCartApplicationService);
    }

    @Test
    @DisplayName("GET /shopping-cart/get-shopping-cart - 成功獲取購物車")
    void should_get_shopping_cart_successfully() throws Exception {
        // Given
        GetShoppingCartDto mockDto = GetShoppingCartDto.builder()
            .shoppingCartId(1L)
            .userId(1L)
            .cartItems(new GetShoppingCartResponse.CartItem[0])
            .build();
        
        when(shoppingCartApplicationService.getShoppingCart(validToken)).thenReturn(mockDto);

        // When & Then
        mockMvc.perform(get("/shopping-cart/get-shopping-cart")
                .header("Authorization", validToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(shoppingCartApplicationService).getShoppingCart(validToken);
    }

    @Test
    @DisplayName("PUT /shopping-cart/update-cart-item-quantity - 成功更新商品數量")
    void should_update_cart_item_quantity_successfully() throws Exception {
        // Given
        doNothing().when(shoppingCartApplicationService).updateCartItemQuantity(anyString(), any(UpdateCartItemQuantityDto.class));

        // When & Then
        mockMvc.perform(put("/shopping-cart/update-cart-item-quantity")
                .header("Authorization", validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        verify(shoppingCartApplicationService).updateCartItemQuantity(eq(validToken), any(UpdateCartItemQuantityDto.class));
    }

    @Test
    @DisplayName("DELETE /shopping-cart/remove-cart-item/{cartItemId} - 成功移除購物車項目")
    void should_remove_cart_item_successfully() throws Exception {
        // Given
        Long cartItemId = 1L;
        doNothing().when(shoppingCartApplicationService).removeCartItem(validToken, cartItemId);

        // When & Then
        mockMvc.perform(delete("/shopping-cart/remove-cart-item/{cartItemId}", cartItemId)
                .header("Authorization", validToken))
                .andExpect(status().isOk());

        verify(shoppingCartApplicationService).removeCartItem(validToken, cartItemId);
    }

    @Test
    @DisplayName("POST /shopping-cart/checkout - 成功結帳")
    void should_checkout_successfully() throws Exception {
        // Given
        doNothing().when(shoppingCartApplicationService).checkout(anyString(), any(CheckoutRequest.class));

        // When & Then
        mockMvc.perform(post("/shopping-cart/checkout")
                .header("Authorization", validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(checkoutRequest)))
                .andExpect(status().isOk());

        verify(shoppingCartApplicationService).checkout(eq(validToken), any(CheckoutRequest.class));
    }

    @Test
    @DisplayName("DELETE /shopping-cart/clear - 成功清空購物車")
    void should_clear_cart_successfully() throws Exception {
        // Given
        doNothing().when(shoppingCartApplicationService).clearCart(validToken);

        // When & Then
        mockMvc.perform(delete("/shopping-cart/clear")
                .header("Authorization", validToken))
                .andExpect(status().isOk());

        verify(shoppingCartApplicationService).clearCart(validToken);
    }    @Test
    @DisplayName("POST /shopping-cart/add-to-cart - 無效請求體應該返回 400")
    void should_return_bad_request_when_invalid_request_body() throws Exception {
        // Given - 完全無效的 JSON 結構，會導致反序列化失敗
        String invalidJson = "{\"invalid\": }"; // 這會導致 JSON 解析錯誤

        // When & Then
        mockMvc.perform(post("/shopping-cart/add-to-cart")
                .header("Authorization", validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(shoppingCartApplicationService);
    }    @Test
    @DisplayName("POST /shopping-cart/add-to-cart - 應用服務拋出異常時應該拋出 ServletException")
    void should_handle_service_exception_appropriately() throws Exception {
        // Given
        doThrow(new RuntimeException("商品不存在")).when(shoppingCartApplicationService)
            .addProductToCart(anyString(), any(AddToCartDto.class));

        // When & Then
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
            jakarta.servlet.ServletException.class, 
            () -> mockMvc.perform(post("/shopping-cart/add-to-cart")
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addToCartRequest)))
        );

        // 驗證異常訊息包含我們拋出的原始異常訊息
        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("商品不存在"));
        verify(shoppingCartApplicationService).addProductToCart(eq(validToken), any(AddToCartDto.class));
    }
}
