package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import com.kai.ninja_ddd_practice.applicationLayer.applicationService.ShoppingCartApplicationService;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.ProductApplicationLayerMapper;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.annotations.AuthorizationValidation;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.AddToCartRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetShoppingCartResponse;
import com.kai.ninja_ddd_practice.interfaceLayer.mapper.ProductInterfaceLayerMapper;
import com.kai.ninja_ddd_practice.interfaceLayer.mapper.ShoppingCartInterfaceLayerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(
            summary = "Add product to cart",
            description = "Add product to cart",
            tags = {"shopping-cart"},
            security = @SecurityRequirement(name = "Authorized")
    )
    @AuthorizationValidation
    public void addProductToCart(@RequestBody AddToCartRequest addToCartRequest) {

        AddToCartDto addToCartDto = ProductInterfaceLayerMapper.convertAddToCartRequestToDto(addToCartRequest);

        productApplicationService.addProductToCart(addToCartDto);
    }

    @GetMapping("/get-shopping-cart")
    @Operation(
            summary = "Get shopping cart",
            description = "Get shopping cart",
            tags = {"shopping-cart"},
            security = @SecurityRequirement(name = "Authorized")
    )
    @AuthorizationValidation
    public GetShoppingCartResponse getShoppingCart(@RequestHeader("Authorization") String token) {
        GetShoppingCartDto getShoppingCartDto = productApplicationService.getShoppingCart(token);
        return ShoppingCartInterfaceLayerMapper.convertGetShoppingCartDtoToResponse(getShoppingCartDto);
    }

}
