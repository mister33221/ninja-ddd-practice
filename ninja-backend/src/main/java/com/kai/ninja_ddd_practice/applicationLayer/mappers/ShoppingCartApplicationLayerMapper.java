package com.kai.ninja_ddd_practice.applicationLayer.mappers;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCartPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItemPure;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetShoppingCartResponse;

/**
 * 在 Application 層中的 Mapper 類別，
 * 負責
 * 1. 將 Domain 層的 Aggregate 轉換為 Application 層的 Dto。
 * 2. 將 Application 層的 Dto 轉換為 Domain 層的 Aggregate。
 */
public class ShoppingCartApplicationLayerMapper {

    private ShoppingCartApplicationLayerMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static GetShoppingCartDto covertShoppingCartToGetShoppingCartDto(ShoppingCartPure shoppingCart) {
        if (shoppingCart == null) {
            return null;
        }
        
        // 轉換購物車項目
        GetShoppingCartResponse.CartItem[] cartItems = shoppingCart.getItems().stream()
                .map(ShoppingCartApplicationLayerMapper::convertCartItemToDto)
                .toArray(GetShoppingCartResponse.CartItem[]::new);
        
        return GetShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getId() != null ? shoppingCart.getId().getValue() : null)
                .userId(shoppingCart.getUserId().getValue())
                .cartItems(cartItems)
                .build();
    }
      private static GetShoppingCartResponse.CartItem convertCartItemToDto(CartItemPure cartItem) {
        return GetShoppingCartResponse.CartItem.builder()
                .id(cartItem.getId() != null ? cartItem.getId().getValue() : null)
                .cartId(null) // Will be set by the response layer if needed
                .productId(cartItem.getProductId().getValue())
                .productName(cartItem.getProductName())
                .productImageURL(cartItem.getProductImageUrl())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getUnitPrice().longValue()) // Convert BigDecimal to Long
                .selected(true) // Default to selected
                .build();
    }
}
