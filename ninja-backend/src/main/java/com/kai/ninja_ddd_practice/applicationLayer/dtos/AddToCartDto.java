package com.kai.ninja_ddd_practice.applicationLayer.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AddToCartDto {

    private Long userId;
    private Long productId;

}
