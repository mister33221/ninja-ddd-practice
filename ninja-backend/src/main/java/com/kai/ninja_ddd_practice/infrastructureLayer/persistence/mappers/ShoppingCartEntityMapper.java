package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCartPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItemPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItemId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.ShoppingCartId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ShoppingCartEntity;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.CartItemEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 購物車領域模型與 JPA Entity 轉換器
 */
@Component
public class ShoppingCartEntityMapper {    /**
     * Entity -> 領域模型
     */
    public ShoppingCartPure toDomain(ShoppingCartEntity entity) {
        if (entity == null) {
            return null;
        }

        List<CartItemPure> items = entity.getItems().stream()
                .map(this::cartItemToDomain)
                .toList();

        return new ShoppingCartPure(
                entity.getId() != null ? ShoppingCartId.of(entity.getId()) : null,
                UserId.of(entity.getUserId()),
                items,
                null, // createdAt - will be set to current time in constructor
                null  // updatedAt - will be set to current time in constructor
        );
    }    /**
     * 領域模型 -> Entity
     */
    public ShoppingCartEntity toEntity(ShoppingCartPure domain) {
        if (domain == null) {
            return null;
        }

        ShoppingCartEntity entity = ShoppingCartEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .userId(domain.getUserId().getValue())
                .build();        // Create CartItemEntity instances and set up bidirectional relationship
        List<CartItemEntity> items = domain.getItems().stream()
                .map(item -> {
                    CartItemEntity cartItem = CartItemEntity.builder()
                            .id(null) // 在新建購物車時，所有項目都應該是新的
                            .productId(item.getProductId().getValue())
                            .productName(item.getProductName())
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .cart(entity) // Set the bidirectional relationship
                            .build();
                    return cartItem;
                })
                .collect(Collectors.toCollection(ArrayList::new)); // Use ArrayList for mutability
        
        entity.setItems(items);
        
        return entity;
    }/**
     * CartItemEntity -> CartItemPure
     */
    private CartItemPure cartItemToDomain(CartItemEntity entity) {
        if (entity == null) {
            return null;
        }

        return CartItemPure.builder()
                .id(entity.getId() != null ? CartItemId.of(entity.getId()) : null)
                .productId(ProductId.of(entity.getProductId()))
                .productName(entity.getProductName())
                .productImageUrl(null) // TODO: Add productImageUrl to CartItemEntity
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .build();
    }    /**
     * 批量轉換：Entity List -> Domain List
     */
    public List<ShoppingCartPure> toDomainList(List<ShoppingCartEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    /**
     * 批量轉換：Domain List -> Entity List
     */
    public List<ShoppingCartEntity> toEntityList(List<ShoppingCartPure> domains) {
        if (domains == null) {
            return List.of();
        }
        return domains.stream()
                .map(this::toEntity)
                .toList();
    }
}
