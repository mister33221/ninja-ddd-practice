package com.kai.ninja_ddd_practice.domainLayer.model.aggregations.product.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.model.aggregations.product.valueObject.ProductCategory;
import com.kai.ninja_ddd_practice.domainLayer.model.aggregations.product.valueObject.ProductDetails;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity // 標記一個類為JPA實體。表示這個類將被映射到 DB 的一個 table。
@Table(name = "products") // 指定實體對應的 table 名稱。
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

    @ManyToOne(fetch = FetchType.LAZY) // 標記一個屬性為多對一關聯。指的是多個 Product 可能屬於同一個 ProductCategory。
    @JoinColumn(name = "category_id") // 指定關聯的外鍵列名。這個 Product table 將會有一個 category_id 的列，用來關聯到 ProductCategory table 的 id 列。
    private ProductCategory category;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    public void updateDetails(ProductDetails newDetails) {  }
    public void changeCategory(ProductCategory newCategory) {  }
    public void updatePrice(BigDecimal newPrice) {  }
}
