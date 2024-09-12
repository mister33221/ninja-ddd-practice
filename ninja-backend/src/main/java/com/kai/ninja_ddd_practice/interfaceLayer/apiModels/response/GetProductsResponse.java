package com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductsResponse {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Long price;
    private String category;

}
