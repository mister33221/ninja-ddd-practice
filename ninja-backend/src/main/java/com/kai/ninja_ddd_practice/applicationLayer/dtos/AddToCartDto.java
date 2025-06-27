package com.kai.ninja_ddd_practice.applicationLayer.dtos;

import lombok.*;

/**
 * Application 層中的 DTO（Data Transfer Object）用於在應用層和接口層之間傳遞數據。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddToCartDto {

    private Long productId;
    private Integer quantity; // 添加數量參數，默認為1
    // 移除 userId，將從 JWT token 中提取

}
