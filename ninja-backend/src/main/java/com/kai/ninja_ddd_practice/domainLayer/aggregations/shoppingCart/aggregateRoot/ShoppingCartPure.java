package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.Money;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItemId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItemPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.ShoppingCartId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Shopping Cart Aggregate Root - Pure Domain Model
 * No JPA/Legacy pollution - only pure domain logic
 */
public class ShoppingCartPure {
    private ShoppingCartId id;
    private UserId userId;
    private List<CartItemPure> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public ShoppingCartPure() {
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with parameters
    public ShoppingCartPure(ShoppingCartId id, UserId userId) {
        this.id = id;
        this.userId = userId;
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor for mapper (used by infrastructure layer)
    public ShoppingCartPure(ShoppingCartId id, UserId userId, List<CartItemPure> items,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.items = new ArrayList<>(items != null ? items : new ArrayList<>());
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
    }

    // Getters
    public ShoppingCartId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public List<CartItemPure> getItems() {
        return new ArrayList<>(items);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }    // Domain methods
    private CartItemId generateTemporaryId() {
        // Generate a temporary ID based on current timestamp and item count
        return CartItemId.of(System.nanoTime() + items.size());
    }

    public void addProduct(ProductPure product, Integer quantity) {
        if (product == null) {
            throw new IllegalArgumentException("商品不能為空");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("數量必須大於 0");
        }

        // Check if product already exists in cart
        Optional<CartItemPure> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity of existing item
            CartItemPure item = existingItem.get();
            items.remove(item);            items.add(item.toBuilder()
                    .quantity(item.getQuantity() + quantity)
                    .build());
        } else {
            // Add new item with temporary ID for domain logic
            items.add(CartItemPure.builder()
                    .id(generateTemporaryId())
                    .productId(product.getId())
                    .productName(product.getDetails().getName())
                    .productImageUrl(product.getImageUrl())
                    .quantity(quantity)
                    .unitPrice(product.getPrice().getAmount())
                    .build());
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void removeProduct(ProductId productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        items.removeIf(item -> item.getProductId().equals(productId));
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProductQuantity(ProductId productId, Integer newQuantity) {
        if (productId == null || newQuantity == null) {
            throw new IllegalArgumentException("Product ID and quantity cannot be null");
        }

        if (newQuantity <= 0) {
            removeProduct(productId);
            return;
        }

        Optional<CartItemPure> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();        if (existingItem.isPresent()) {
            CartItemPure item = existingItem.get();
            items.remove(item);
            items.add(item.toBuilder()
                    .quantity(newQuantity)
                    .build());
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("商品不存在於購物車中");
        }
    }

    public void clear() {
        this.items.clear();
        this.updatedAt = LocalDateTime.now();
    }

    public Money getTotalAmount() {
        BigDecimal total = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Money(total);
    }

    public Integer getItemCount() {
        return items.stream()
                .mapToInt(CartItemPure::getQuantity)
                .sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Static factory method
    public static ShoppingCartPure createNewCart(UserId userId) {
        return new ShoppingCartPure(null, userId); // ID will be generated when persisted
    }
}
