package com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddToCartRequest {

    private Long productId;
    private Integer quantity; // 添加數量參數

}
