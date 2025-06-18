package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductDetails;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 產品 JPA 實體 - 用於 ShoppingCart 等舊有功能
 * 注意：這是一個遺留的實體，新的代碼應該使用 ProductEntity 和 ProductPure
 * TODO: 未來應該重構 ShoppingCart 來使用 ProductEntity，然後移除這個類
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductLegacyEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductDetails details;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategoryEntity category;

    @Column(nullable = false)
    private String status;

    @Column(name = "image_url")
    private String imageUrl;

    public ProductLegacyEntity(ProductDetails details, BigDecimal price, int stockQuantity, ProductCategoryEntity category, String status, String imageUrl) {
        this.details = details;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public void updateStock(int quantity) {
        if (this.stockQuantity + quantity < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stockQuantity += quantity;
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.price = newPrice;
    }
}
