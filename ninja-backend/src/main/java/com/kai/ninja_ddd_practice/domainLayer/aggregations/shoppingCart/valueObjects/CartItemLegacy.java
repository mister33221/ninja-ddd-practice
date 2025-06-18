// This class has been replaced by CartItemPure and CartItemEntity in the Anti-Corruption Layer refactoring
// Keeping as legacy backup but removing @Entity annotation to avoid conflicts

package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects;

import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.Product;
// import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// @Entity  // Removed to avoid conflicts with new CartItemEntity
// @Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemLegacy {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(name = "cart_id", nullable = false)
    private Long cartId;

//    @Column(name = "product_id", nullable = false)
//    private Long productId;

    // @ManyToOne
    // @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    // @Column(nullable = false)
    private int quantity;

    // @Column(nullable = false)
    private BigDecimal price;

    public CartItemLegacy(Long cartId, Product product, int quantity, BigDecimal price) {
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

    public Object getProductId() {
        return product.getId();
    }
}
