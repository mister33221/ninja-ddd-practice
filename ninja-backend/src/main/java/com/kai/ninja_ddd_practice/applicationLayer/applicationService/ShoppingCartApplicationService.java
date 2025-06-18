package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateCartItemQuantityDto;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.ShoppingCartApplicationLayerMapper;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCartPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ShoppingCartPureRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.JwtUtil;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.request.CheckoutRequest;
import com.kai.ninja_ddd_practice.interfaceLayer.apiModels.response.GetShoppingCartResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 購物車應用服務
 * 處理購物車相關的業務邏輯
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShoppingCartApplicationService {

    private final ShoppingCartPureRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final JwtUtil jwtUtil;

    /**
     * 添加商品到購物車
     */
    public void addToCart(String token, AddToCartDto dto) {
        try {
            // 從 JWT token 中提取用戶 ID
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            // 驗證 token 並提取用戶 ID
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.extractUserId(token);
            
            log.info("Adding product {} to cart for user {} (extracted from token)", dto.getProductId(), userId);
            
            // 1. 驗證輸入參數
            if (dto.getProductId() == null) {
                throw new IllegalArgumentException("Product ID must not be null");
            }
            
            // 2. 獲取商品信息
            UserId userIdObj = UserId.of(userId);
            ProductId productId = ProductId.of(dto.getProductId());
            
            Optional<ProductPure> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new IllegalArgumentException("Product not found with ID: " + dto.getProductId());
            }
            
            ProductPure product = productOpt.get();
            
            // 3. 獲取或創建購物車
            Optional<ShoppingCartPure> cartOpt = shoppingCartRepository.findByUserId(userIdObj);
            ShoppingCartPure cart;
            
            if (cartOpt.isPresent()) {
                cart = cartOpt.get();
            } else {
                // 創建新購物車 (ID 為 null，會在保存時自動生成)
                cart = new ShoppingCartPure(null, userIdObj);
            }
            
            // 4. 添加商品到購物車 (使用指定數量，默認為1)
            int quantity = dto.getQuantity() != null && dto.getQuantity() > 0 ? dto.getQuantity() : 1;
            cart.addProduct(product, quantity);
            
            // 5. 保存購物車
            shoppingCartRepository.save(cart);
            
            log.info("Successfully added product {} to cart for user {}", dto.getProductId(), userId);
            
        } catch (Exception e) {            log.error("Error adding product {} to cart: {}", dto.getProductId(), e.getMessage(), e);
            throw new RuntimeException("Failed to add product to cart: " + e.getMessage(), e);
        }
    }

    /**
     * 添加商品到購物車 (Controller 使用的方法名)
     */
    public void addProductToCart(String token, AddToCartDto dto) {
        addToCart(token, dto);
    }

    /**
     * 從購物車移除商品
     */
    public void removeFromCart(Long userId, Long productId) {
        // TODO: Implement remove from cart logic
        throw new UnsupportedOperationException("removeFromCart method not implemented yet");
    }

    /**
     * 移除購物車項目 (Controller 使用的方法名)
     */
    public void removeCartItem(Long cartItemId) {
        // TODO: Implement remove cart item logic
        throw new UnsupportedOperationException("removeCartItem method not implemented yet");
    }

    /**
     * 獲取用戶的購物車
     */
    public Object getCartByUserId(Long userId) {
        // TODO: Implement get cart by user id logic
        throw new UnsupportedOperationException("getCartByUserId method not implemented yet");
    }    /**
     * 獲取購物車 (Controller 使用的方法名)
     */
    public GetShoppingCartDto getShoppingCart(String token) {
        log.info("Getting shopping cart for token: {}", token != null ? "***" : "null");
        
        try {
            // 從 JWT token 中提取用戶 ID
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            // 驗證 token 並提取用戶 ID
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.extractUserId(token);
            
            log.info("Extracted user ID {} from token", userId);
            
            Optional<ShoppingCartPure> cartOpt = shoppingCartRepository.findByUserId(UserId.of(userId));
            
            if (cartOpt.isEmpty()) {
                // Create and return empty cart if not exists
                log.info("No cart found for user {}, returning empty cart", userId);
                return createEmptyCartDto(userId);
            }
            
            ShoppingCartPure cart = cartOpt.get();
            // Use mapper to convert domain model to DTO
            GetShoppingCartDto result = ShoppingCartApplicationLayerMapper.covertShoppingCartToGetShoppingCartDto(cart);
            
            log.info("Retrieved cart for user {} with {} items", userId, cart.getItems().size());
            return result;
            
        } catch (Exception e) {
            log.error("Error getting shopping cart: {}", e.getMessage(), e);
            // For JWT-related errors, rethrow them to be handled by the controller
            if (e.getMessage().contains("JWT") || e.getMessage().contains("token") || e.getMessage().contains("Token")) {
                throw e;
            }
            // For other errors, return empty cart with default user
            return createEmptyCartDto(7777L);
        }
    }
    
    /**
     * 創建空購物車 DTO
     */
    private GetShoppingCartDto createEmptyCartDto(Long userId) {
        return GetShoppingCartDto.builder()
                .shoppingCartId(null)
                .userId(userId)
                .cartItems(new GetShoppingCartResponse.CartItem[0])
                .build();
    }

    /**
     * 更新購物車項目數量
     */
    public void updateCartItemQuantity(UpdateCartItemQuantityDto dto) {
        // TODO: Implement update cart item quantity logic
        throw new UnsupportedOperationException("updateCartItemQuantity method not implemented yet");
    }

    /**
     * 結帳
     */
    public void checkout(String token, CheckoutRequest checkoutRequest) {
        // TODO: Implement checkout logic
        throw new UnsupportedOperationException("checkout method not implemented yet");
    }

    /**
     * 清空購物車
     */
    public void clearCart(Long userId) {
        // TODO: Implement clear cart logic
        throw new UnsupportedOperationException("clearCart method not implemented yet");
    }
}
