package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/prudct-list")
    @Operation(summary = "Get product list", description = "Get product list", tags = { "Product" })
    public String getProductList() {
        return "product list";
    }

}
