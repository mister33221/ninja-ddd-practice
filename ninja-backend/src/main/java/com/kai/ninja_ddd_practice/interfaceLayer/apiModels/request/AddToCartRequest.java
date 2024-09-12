package com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddToCartRequest {

    private Long userId;
    private Long productId;

}
