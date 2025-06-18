package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 購物車 JPA Entity - 僅用於基礎設施層
 */
@Entity
@Table(name = "shopping_carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "cartId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItemEntity> items = new ArrayList<>();

    /**
     * 便利方法：新增項目
     */
    public void addItem(CartItemEntity item) {
        items.add(item);
        item.setCartId(this.id);
    }

    /**
     * 便利方法：移除項目
     */
    public void removeItem(CartItemEntity item) {
        items.remove(item);
        item.setCartId(null);
    }
}
