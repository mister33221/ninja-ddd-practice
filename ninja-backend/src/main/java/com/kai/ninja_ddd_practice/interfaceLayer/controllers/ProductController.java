package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.ninja_ddd_practice.applicationLayer.applicationService.ProductApplicationService;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetProductCardsDto;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetProductsResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductApplicationService productApplicationService;
    //    private final ProductControllerMapper productControllerMapper;
    private final ObjectMapper objectMapper;

    public ProductController(ProductApplicationService productApplicationService, ObjectMapper objectMapper) {
        this.productApplicationService = productApplicationService;
        this.objectMapper = objectMapper;
//        this.productControllerMapper = productControllerMapper;
    }

    @GetMapping("/get-product-cards")
    @Operation(summary = "Get product cards", description = "Get product cards", tags = {"product"})
    public List<GetProductsResponse> getProductList() {

        List<GetProductCardsDto> productCardsDtos = productApplicationService.getProductCards();

        return productCardsDtos.stream().map(product -> objectMapper.convertValue(product, GetProductsResponse.class)).toList();
    }

}
