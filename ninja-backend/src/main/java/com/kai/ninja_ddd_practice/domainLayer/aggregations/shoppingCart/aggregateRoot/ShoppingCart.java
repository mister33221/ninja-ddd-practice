package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> items = new ArrayList<>();

    public ShoppingCart(Long userId) {
        this.userId = userId;
    }

    public void addProduct(Product product, int quantity) {
        items.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.incrementQuantity(quantity, product.getPrice()),
                        () -> items.add(new CartItem(this.id, product.getId(), quantity, product.getPrice()))
                );
    }

    public void removeProduct(Long productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
