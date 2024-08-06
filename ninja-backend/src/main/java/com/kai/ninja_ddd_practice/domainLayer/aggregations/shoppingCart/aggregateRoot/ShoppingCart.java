package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItem;
import jakarta.persistence.*;
import lombok.*;

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

    public void addItem(Long productId, int quantity) {  }
    public void removeItem(Long productId) {  }
    public void updateItemQuantity(Long productId, int newQuantity) {  }
    public void clear() {  }
}
