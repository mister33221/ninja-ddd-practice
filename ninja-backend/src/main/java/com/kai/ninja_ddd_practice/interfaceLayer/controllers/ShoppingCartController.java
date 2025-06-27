package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import com.kai.ninja_ddd_practice.applicationLayer.applicationService.ShoppingCartApplicationService;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateCartItemQuantityDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.principal.JwtUserPrincipal;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.AddToCartRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.CheckoutRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.UpdaateCartItemQuantityRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetShoppingCartResponse;
import com.kai.ninja_ddd_practice.interfaceLayer.mapper.ProductInterfaceLayerMapper;
import com.kai.ninja_ddd_practice.interfaceLayer.mapper.ShoppingCartInterfaceLayerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 在 interface 層的 controller，只負責
 * 1. 接收 HTTP 請求
 * 2. 驗證請求
 * 3. 進行必要的轉換
 * 4. 進行防腐
 * 5. 調用 application service
 */

@RestController
@RequestMapping("/shopping-cart")
@CrossOrigin(origins = "*")
public class ShoppingCartController {

    private final ShoppingCartApplicationService shoppingCartApplicationService;

    public ShoppingCartController(ShoppingCartApplicationService shoppingCartApplicationService) {
        this.shoppingCartApplicationService = shoppingCartApplicationService;
    }    @PostMapping("/add-to-cart")
    @Operation(
            summary = "Add product to cart",
            description = "Add product to cart",
            tags = {"shopping-cart"},
            security = @SecurityRequirement(name = "Authorized")
    )
    public void addProductToCart(@AuthenticationPrincipal JwtUserPrincipal principal,
                                @RequestBody AddToCartRequest addToCartRequest) {

//      1. 登入時：用戶資訊被編碼到 JWT Token 的 Claims 中
//      2. 每次請求時：JwtAuthenticationFilter 從 JWT Token 中解析 Claims 並創建 JwtUserPrincipal 對象
//      3. Controller 調用時：Spring Security 自動將 JwtUserPrincipal 注入到 @AuthenticationPrincipal 參數中

        // 這個註解會跨 DDD 架構，一路到取回資料，以明確了解資料的流向
        // 1. 從 JWT 中取得使用者 ID。這裡使用 "強型別" 的方式，確保 UserId 是正確的類型。
        UserId userId = UserId.of(principal.getUserId());
        // 2. 將 AddToCartRequest 轉換為 AddToCartDto，這是所謂的防腐層（Anti-Corruption Layer）模式，將物件轉為 Data Transfer Object (DTO) 以便於傳輸和處理。
        AddToCartDto addToCartDto = ProductInterfaceLayerMapper.convertAddToCartRequestToDto(addToCartRequest);

        shoppingCartApplicationService.addProductToCart(userId, addToCartDto);
    }

    @GetMapping("/get-shopping-cart")
    @Operation(
            summary = "Get shopping cart",
            description = "Get shopping cart",
            tags = {"shopping-cart"},
            security = @SecurityRequirement(name = "Authorized")
    )
    public GetShoppingCartResponse getShoppingCart(@AuthenticationPrincipal JwtUserPrincipal principal) {
        UserId userId = UserId.of(principal.getUserId());
        GetShoppingCartDto getShoppingCartDto = shoppingCartApplicationService.getShoppingCart(userId);
        return ShoppingCartInterfaceLayerMapper.convertGetShoppingCartDtoToResponse(getShoppingCartDto);
    }

    @PutMapping("/update-cart-item-quantity")
    @Operation(
            summary = "Update cart item quantity",
            description = "Update cart item quantity",
            tags = {"shopping-cart"},
            security = @SecurityRequirement(name = "Authorized")
    )
    public void updateCartItemQuantity(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody UpdaateCartItemQuantityRequest updaateCartItemQuantityRequest) {
        UserId userId = UserId.of(principal.getUserId());
        UpdateCartItemQuantityDto updateCartItemQuantityDto = ShoppingCartInterfaceLayerMapper.convertUpdateCartItemQuantityRequestToDto(updaateCartItemQuantityRequest);
        shoppingCartApplicationService.updateCartItemQuantity(userId, updateCartItemQuantityDto);
    }

    @DeleteMapping("/remove-cart-item/{cartItemId}")
    @Operation(
            summary = "Remove cart item",
            description = "Remove cart item",
            tags = {"shopping-cart"},
            security = @SecurityRequirement(name = "Authorized")
    )
    public void removeCartItem(@AuthenticationPrincipal JwtUserPrincipal principal,
                              @PathVariable Long cartItemId) {
        UserId userId = UserId.of(principal.getUserId());
        shoppingCartApplicationService.removeCartItem(userId, cartItemId);
    }

    @PostMapping("/checkout")
    @Operation(
            summary = "Checkout",
            description = "Checkout",
            tags = {"shopping-cart"},
            security = @SecurityRequirement(name = "Authorized")
    )
    public void checkout(@AuthenticationPrincipal JwtUserPrincipal principal, 
                        @RequestBody CheckoutRequest checkoutRequest) {
        UserId userId = UserId.of(principal.getUserId());
//        CheckoutDto checkoutDto = ShoppingCartInterfaceLayerMapper.convertCheckoutRequestToDto(checkoutRequest);
        shoppingCartApplicationService.checkout(userId, checkoutRequest);
    }

    @DeleteMapping("/clear")
    @Operation(
            summary = "Clear shopping cart",
            description = "Clear all items from shopping cart",
            tags = {"shopping-cart"},
            security = @SecurityRequirement(name = "Authorized")
    )
    public void clearCart(@AuthenticationPrincipal JwtUserPrincipal principal) {
        UserId userId = UserId.of(principal.getUserId());
        shoppingCartApplicationService.clearCart(userId);
    }

}
