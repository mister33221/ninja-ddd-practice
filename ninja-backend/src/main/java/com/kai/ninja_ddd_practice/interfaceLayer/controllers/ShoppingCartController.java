package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import com.kai.ninja_ddd_practice.applicationLayer.applicationService.ShoppingCartApplicationService;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.ProductApplicationMapper;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.annotations.AuthorizationValidation;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.AddToCartRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping-cart")
@CrossOrigin(origins = "*")
public class ShoppingCartController {

    private final ShoppingCartApplicationService productApplicationService;

    public ShoppingCartController(ShoppingCartApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @PostMapping("/add-to-cart")
    @Operation(summary = "Add product to cart", description = "Add product to cart", tags = {"product"})
    @AuthorizationValidation
    public void addProductToCart(@RequestBody AddToCartRequest addToCartRequest) {

        AddToCartDto addToCartDto = ProductApplicationMapper.convertAddToCartRequestToDto(addToCartRequest);

        productApplicationService.addProductToCart(addToCartDto);
    }

}
