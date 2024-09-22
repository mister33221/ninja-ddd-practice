package com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutRequest {

//    export interface CheckoutRequest {
//        shoppingCartId: number;
//        userId: number;
//        cartItems: CartItem[];
//    }
//
//    export interface CartItem {
//        id: number;
//        cartId: number;
//        productId: number;
//        productName: string;
//        productImageURL: string;
//        quantity: number;
//        price: number;
//        selected?: boolean;
//    }


    private Long shoppingCartId;
    private Long userId;
    private List<CartItem> cartItems;

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
        private Integer quantity;
        private Double price;
        private Boolean selected;
    }
}
