package com.kai.ninja_ddd_practice.domainLayer.model.aggregations.product.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.model.aggregations.product.valueObject.Image;
import com.kai.ninja_ddd_practice.domainLayer.model.aggregations.product.valueObject.ProductDetails;
import com.kai.ninja_ddd_practice.domainLayer.model.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Embedded
    @Enumerated(EnumType.STRING) // 指定枚舉類型的映射策略。這裡使用的是字符串形式。表示雖然我在這邊的型別是枚舉類型，但在 DB 中，它將被映射為字符串形式。
    @Column(nullable = false)
    private ProductStatus productStatus;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
//    @Column(name = "image_url")
    private List<Image> images;

    public void updateDetails(ProductDetails newDetails) {  }
    public void updatePrice(BigDecimal newPrice) {  }
    public void updateCategoryId(Long newCategoryId) {  }
}
