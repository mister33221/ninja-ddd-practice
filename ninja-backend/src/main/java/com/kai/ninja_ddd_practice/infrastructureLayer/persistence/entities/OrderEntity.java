package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 訂單 JPA Entity - 僅用於基礎設施層
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItemEntity> items = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // Payment info embedded
    @Column(name = "payment_method")
    private String paymentMethod;
    
    @Column(name = "payment_amount", precision = 10, scale = 2)
    private BigDecimal paymentAmount;
    
    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * 便利方法：新增項目
     */
    public void addItem(OrderItemEntity item) {
        items.add(item);
        item.setOrderId(this.id);
    }

    /**
     * 便利方法：移除項目
     */
    public void removeItem(OrderItemEntity item) {
        items.remove(item);
        item.setOrderId(null);
    }

    /**
     * 訂單狀態枚舉（用於 JPA）
     */
    public enum OrderStatusEnum {
        PENDING, PAID, PROCESSING, SHIPPED, COMPLETED, CANCELLED
    }
}
