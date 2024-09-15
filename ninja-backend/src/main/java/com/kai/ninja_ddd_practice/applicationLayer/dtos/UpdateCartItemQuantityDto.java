package com.kai.ninja_ddd_practice.applicationLayer.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartItemQuantityDto {

    private Long id;
    private Long cartId;
    private Long productId;
    private String productName;
    private String productImageURL;
    private Integer quantity;
    private BigDecimal price;

}
