package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    public CartItem(Long cardId, Long productId, int quantity, BigDecimal price) {
        this.cartId = cardId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public void incrementQuantity(int amount, BigDecimal price) {
        this.quantity += amount;
        this.price = this.price.add(price);
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
