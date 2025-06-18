// This class has been replaced by ShoppingCartPure and ShoppingCartEntity in the Anti-Corruption Layer refactoring
// Keeping as legacy backup but removing @Entity annotation to avoid conflicts

package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot;

import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.Product;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItemLegacy;
// import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// @Entity  // Removed to avoid conflicts with new ShoppingCartEntity
// @Table(name = "shopping_carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartLegacy {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(name = "user_id", nullable = false)
    private Long userId;

    // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "cart_id")
    private List<CartItemLegacy> items = new ArrayList<>();

    public ShoppingCartLegacy(Long userId) {
        this.userId = userId;
    }

    public void addProduct(Product product, int quantity) {
        items.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.incrementQuantity(quantity, product.getPrice()),
                        () -> items.add(new CartItemLegacy(this.id, product, quantity, product.getPrice()))
                );
    }


    public Optional<CartItemLegacy> getCartItemById(Long id) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    public List<CartItemLegacy> getCartItems() {
        return items;
    }
}
