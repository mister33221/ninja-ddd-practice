package com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItem;

public interface CartItemRepository {
    void deleteById(Long cartItemId);

    void delete(CartItem cartItem);
}
