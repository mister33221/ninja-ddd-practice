package com.kai.ninja_ddd_practice.domainLayer.model.aggregations.product.valueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Column(name = "image_url", nullable = false)
    private String url;

    // 其他屬性和方法
}
