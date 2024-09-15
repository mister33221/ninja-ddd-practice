package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
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

//    @Column(name = "product_id", nullable = false)
//    private Long productId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    public CartItem(Long cartId, Product product, int quantity, BigDecimal price) {
        this.cartId = cartId;
        this.product = product;
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

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
