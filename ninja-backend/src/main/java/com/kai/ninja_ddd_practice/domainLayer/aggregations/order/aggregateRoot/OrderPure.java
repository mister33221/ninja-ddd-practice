package com.kai.ninja_ddd_practice.domainLayer.aggregations.order.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects.*;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 訂單聚合根 - 純領域模型
 * 不含任何基礎設施依賴，專注於業務邏輯
 */
@Value
@Builder(toBuilder = true)
@Jacksonized
public class OrderPure {
    
    OrderId id;
    
    @NonNull
    UserId userId;
    
    @NonNull
    @Builder.Default
    List<OrderItemPure> items = List.of();
    
    @NonNull
    @Builder.Default
    OrderStatus status = OrderStatus.PENDING;
    
    @NonNull
    BigDecimal totalAmount;
    
    PaymentInfoPure paymentInfo;
    
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
    
    LocalDateTime updatedAt;

    /**
     * 新增訂單項目
     */
    public OrderPure addItem(ProductId productId, String productName, int quantity, BigDecimal unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be positive");
        }
        
        // 檢查是否已存在相同產品
        Optional<OrderItemPure> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
                
        if (existingItem.isPresent()) {
            // 更新現有項目數量
            List<OrderItemPure> updatedItems = items.stream()
                    .map(item -> item.getProductId().equals(productId) 
                            ? item.toBuilder().quantity(item.getQuantity() + quantity).build()
                            : item)
                    .toList();
                    
            return this.toBuilder()
                    .items(updatedItems)
                    .totalAmount(calculateTotalAmount(updatedItems))
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else {
            // 新增項目
            OrderItemPure newItem = OrderItemPure.builder()
                    .productId(productId)
                    .productName(productName)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .build();
                    
            List<OrderItemPure> updatedItems = new java.util.ArrayList<>(items);
            updatedItems.add(newItem);
            
            return this.toBuilder()
                    .items(updatedItems)
                    .totalAmount(calculateTotalAmount(updatedItems))
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * 更新訂單狀態
     */
    public OrderPure updateStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        
        // 檢查狀態轉換是否合法
        if (!isValidStatusTransition(this.status, newStatus)) {
            throw new IllegalStateException(
                String.format("Invalid status transition from %s to %s", this.status, newStatus)
            );
        }
        
        return this.toBuilder()
                .status(newStatus)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 處理付款
     */
    public OrderPure processPayment(PaymentInfoPure paymentInfo) {
        if (paymentInfo == null) {
            throw new IllegalArgumentException("Payment info cannot be null");
        }
        
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only process payment for pending orders");
        }
        
        return this.toBuilder()
                .paymentInfo(paymentInfo)
                .status(OrderStatus.PAID)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 移除訂單項目
     */
    public OrderPure removeItem(ProductId productId) {
        List<OrderItemPure> updatedItems = items.stream()
                .filter(item -> !item.getProductId().equals(productId))
                .toList();
                
        return this.toBuilder()
                .items(updatedItems)
                .totalAmount(calculateTotalAmount(updatedItems))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 更新項目數量
     */
    public OrderPure updateItemQuantity(ProductId productId, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        
        if (newQuantity == 0) {
            return removeItem(productId);
        }
        
        List<OrderItemPure> updatedItems = items.stream()
                .map(item -> item.getProductId().equals(productId) 
                        ? item.toBuilder().quantity(newQuantity).build()
                        : item)
                .toList();
                
        return this.toBuilder()
                .items(updatedItems)
                .totalAmount(calculateTotalAmount(updatedItems))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 取消訂單
     */
    public OrderPure cancel() {
        if (this.status == OrderStatus.COMPLETED || this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel completed or already cancelled order");
        }
        
        return updateStatus(OrderStatus.CANCELLED);
    }

    /**
     * 完成訂單
     */
    public OrderPure complete() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Can only complete shipped orders");
        }
        
        return updateStatus(OrderStatus.COMPLETED);
    }

    /**
     * 檢查是否可以修改
     */
    public boolean isModifiable() {
        return status == OrderStatus.PENDING;
    }

    /**
     * 取得項目數量
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * 取得總商品數量
     */
    public int getTotalQuantity() {
        return items.stream()
                .mapToInt(OrderItemPure::getQuantity)
                .sum();
    }

    /**
     * 計算總金額
     */
    private BigDecimal calculateTotalAmount(List<OrderItemPure> items) {
        return items.stream()
                .map(OrderItemPure::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 檢查狀態轉換是否合法
     */
    private boolean isValidStatusTransition(OrderStatus from, OrderStatus to) {
        return switch (from) {
            case PENDING -> to == OrderStatus.PAID || to == OrderStatus.CANCELLED;
            case PAID -> to == OrderStatus.PROCESSING || to == OrderStatus.CANCELLED;
            case PROCESSING -> to == OrderStatus.SHIPPED || to == OrderStatus.CANCELLED;
            case SHIPPED -> to == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false; // 終態，不可轉換
        };
    }
}
