package com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {
    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "reorder_threshold", nullable = false)
    private int reorderThreshold;
}
