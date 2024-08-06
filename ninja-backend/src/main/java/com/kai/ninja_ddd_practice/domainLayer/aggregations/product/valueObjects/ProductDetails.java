package com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetails {
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;
}
