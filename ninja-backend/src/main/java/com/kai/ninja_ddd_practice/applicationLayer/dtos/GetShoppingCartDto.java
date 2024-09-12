package com.kai.ninja_ddd_practice.applicationLayer.dtos;

import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetShoppingCartResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetShoppingCartDto {
    private Long shoppingCartId;
    private Long userId;
    private GetShoppingCartResponse.CartItem[] cartItems;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CartItem {
        private Long id;
        private Long cartId;
        private Long productId;
        private String productName;
        private String productImageURL;
        private int quantity;
        private Long price;
        private boolean selected;
    }
}
