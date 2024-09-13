package com.kai.ninja_ddd_practice.applicationLayer.mappers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCart;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItem;

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

    public static GetShoppingCartDto covertShoppingCartToGetShoppingCartDto(ShoppingCart shoppingCart) {
        ObjectNode node = objectMapper.createObjectNode();

        node.put("shoppingCartId", shoppingCart.getId());
        node.put("userId", shoppingCart.getUserId());

        ArrayNode cartItemsNode = node.putArray("cartItems");
        for (CartItem cartItem : shoppingCart.getItems()) {
            ObjectNode cartItemNode = cartItemsNode.addObject();
            cartItemNode.put("id", cartItem.getId());
            cartItemNode.put("cartId", cartItem.getCartId());
            cartItemNode.put("productId", cartItem.getProduct().getId());
            cartItemNode.put("productName", cartItem.getProduct().getDetails().getName());
            cartItemNode.put("productImageURL", cartItem.getProduct().getImageUrl());
            cartItemNode.put("quantity", cartItem.getQuantity());
            cartItemNode.put("price", cartItem.getPrice());
            cartItemNode.put("selected", true);
        }

        return objectMapper.convertValue(node, GetShoppingCartDto.class);
        }
}
