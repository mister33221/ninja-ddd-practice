package com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductDetails;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity // 標記一個類為JPA實體。表示這個類將被映射到 DB 的一個 table。
@Table(name = "product") // 指定實體對應的 table 名稱。
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id // 標記一個屬性為主鍵。也就是這個屬性，將會作為這個 table 的主鍵。
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 定義主鍵的生成策略。IDENTITY 表示自動增長。
    private Long id;

    @Embedded // 標記一個屬性為嵌入式對象。將會看到另外一個 Class 中被標記為 Embeddable。在 DB 中，他們都是在同一個 table 中。但在程式碼中為了提高重用性、可讀性，我們將他們分開為兩個 Class。
    private ProductDetails details;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

//    @Column(name = "category_id", nullable = false)
//    private Long categoryId;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

//    這樣會導致你的 enum 中有幾個參數，就會有幾個 column，我的有 status，還有 statusDescription。
//    但我只想要 status，所以改成直接用 String
//    @Embedded
//    @Enumerated(EnumType.STRING) // 指定枚舉類型的映射策略。這裡使用的是字符串形式。表示雖然我在這邊的型別是枚舉類型，但在 DB 中，它將被映射為字符串形式。
//    @Column(nullable = false)
//    private ProductStatus productStatus;
    @Column(nullable = false)
    private String status;

//    @ElementCollection
//    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private String imageUrl;

    public Product(ProductDetails details, BigDecimal price, int stockQuantity, ProductCategory category, String status, String imageUrl) {
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
