package com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetShoppingCartResponse {

    private Long shoppingCartId;
    private Long userId;
    private CartItem[] cartItems;

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
