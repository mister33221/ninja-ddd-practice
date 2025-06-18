package com.kai.ninja_ddd_practice.applicationLayer.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddToCartDto {

    private Long productId;
    private Integer quantity; // 添加數量參數，默認為1
    // 移除 userId，將從 JWT token 中提取

}
