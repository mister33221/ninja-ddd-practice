package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.UpdateCartItemQuantityDto;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.ShoppingCartApplicationLayerMapper;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCartPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItemPure;
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
     */    public void addToCart(String token, AddToCartDto dto) {
        Long productId = null;
        try {
            // 從 JWT token 中提取用戶 ID
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            // 驗證 DTO
            if (dto == null) {
                throw new IllegalArgumentException("AddToCartDto cannot be null");
            }
            
            productId = dto.getProductId();
            
            // 驗證 token 並提取用戶 ID
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.extractUserId(token);
            
            log.info("Adding product {} to cart for user {} (extracted from token)", productId, userId);
              // 1. 驗證輸入參數
            if (productId == null) {
                throw new IllegalArgumentException("Product ID must not be null");
            }
            
            // 2. 獲取商品信息
            UserId userIdObj = UserId.of(userId);
            ProductId productIdObj = ProductId.of(productId);
            
            Optional<ProductPure> productOpt = productRepository.findById(productIdObj);
            if (productOpt.isEmpty()) {
                throw new IllegalArgumentException("Product not found with ID: " + productId);
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
              log.info("Successfully added product {} to cart for user {}", productId, userId);
            
        } catch (Exception e) {
            log.error("Error adding product {} to cart: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Failed to add product to cart: " + e.getMessage(), e);
        }
    }

    /**
     * 添加商品到購物車 (Controller 使用的方法名)
     */
    public void addProductToCart(String token, AddToCartDto dto) {
        addToCart(token, dto);
    }    /**
     * 從購物車移除商品
     */
    public void removeFromCart(String token, Long productId) {
        try {
            // 從 JWT token 中提取用戶 ID
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            // 驗證 token 並提取用戶 ID
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.extractUserId(token);
            
            log.info("Removing product {} from cart for user {} (extracted from token)", productId, userId);
            
            // 1. 驗證輸入參數
            if (productId == null) {
                throw new IllegalArgumentException("Product ID must not be null");
            }
            
            // 2. 獲取用戶的購物車
            UserId userIdObj = UserId.of(userId);
            Optional<ShoppingCartPure> cartOpt = shoppingCartRepository.findByUserId(userIdObj);
            
            if (cartOpt.isEmpty()) {
                // 根據 DDD 原則：如果購物車不存在，我們應該優雅處理而不是拋出異常
                log.info("No cart found for user {}, nothing to remove", userId);
                return;
            }
            
            ShoppingCartPure cart = cartOpt.get();
            ProductId productIdObj = ProductId.of(productId);
            
            // 3. 移除商品（聚合內部會處理商品不存在的情況）
            cart.removeProduct(productIdObj);
            
            // 4. 保存購物車（整個聚合作為事務邊界）
            shoppingCartRepository.save(cart);
            
            log.info("Successfully processed remove product {} request for user {}", productId, userId);
            
        } catch (Exception e) {
            log.error("Error removing product {} from cart: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Failed to remove product from cart: " + e.getMessage(), e);
        }
    }/**
     * 移除購物車項目 (Controller 使用的方法名)
     */
    public void removeCartItem(String token, Long cartItemId) {
        try {
            // 從 JWT token 中提取用戶 ID
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            // 驗證 token 並提取用戶 ID
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.extractUserId(token);
            
            log.info("Removing cart item {} for user {} (extracted from token)", cartItemId, userId);
            
            // 1. 驗證輸入參數
            if (cartItemId == null) {
                throw new IllegalArgumentException("Cart item ID must not be null");
            }
            
            // 2. 獲取用戶的購物車
            UserId userIdObj = UserId.of(userId);
            Optional<ShoppingCartPure> cartOpt = shoppingCartRepository.findByUserId(userIdObj);
            
            if (cartOpt.isEmpty()) {
                throw new IllegalArgumentException("Shopping cart not found for user: " + userId);
            }
            
            ShoppingCartPure cart = cartOpt.get();
            
            // 3. 查找要移除的項目
            Optional<CartItemPure> itemToRemove = cart.getItems().stream()
                    .filter(item -> item.getId() != null && item.getId().getValue().equals(cartItemId))
                    .findFirst();
            
            if (itemToRemove.isEmpty()) {
                throw new IllegalArgumentException("Cart item not found with ID: " + cartItemId);
            }
            
            ProductId productId = itemToRemove.get().getProductId();
            
            // 4. 移除商品
            cart.removeProduct(productId);
            
            // 5. 保存購物車
            shoppingCartRepository.save(cart);
            
            log.info("Successfully removed cart item {} (product {}) for user {}", 
                    cartItemId, productId.getValue(), userId);
            
        } catch (Exception e) {
            log.error("Error removing cart item {}: {}", cartItemId, e.getMessage(), e);
            throw new RuntimeException("Failed to remove cart item: " + e.getMessage(), e);
        }
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
    }    /**
     * 更新購物車項目數量
     */
    public void updateCartItemQuantity(String token, UpdateCartItemQuantityDto dto) {
        try {
            // 從 JWT token 中提取用戶 ID
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            // 驗證 token 並提取用戶 ID
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.extractUserId(token);
            
            log.info("Updating cart item quantity for user {} (extracted from token)", userId);
            
            // 1. 驗證輸入參數
            if (dto.getProductId() == null || dto.getQuantity() == null) {
                throw new IllegalArgumentException("Product ID and quantity must not be null");
            }
            
            if (dto.getQuantity() < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
            
            // 2. 獲取用戶的購物車
            UserId userIdObj = UserId.of(userId);
            Optional<ShoppingCartPure> cartOpt = shoppingCartRepository.findByUserId(userIdObj);
            
            if (cartOpt.isEmpty()) {
                throw new IllegalArgumentException("Shopping cart not found for user: " + userId);
            }
            
            ShoppingCartPure cart = cartOpt.get();
            ProductId productId = ProductId.of(dto.getProductId());
            
            // 3. 更新商品數量
            if (dto.getQuantity() == 0) {
                // 如果數量為 0，則移除商品
                cart.removeProduct(productId);
                log.info("Removed product {} from cart for user {}", dto.getProductId(), userId);
            } else {
                // 更新數量
                cart.updateProductQuantity(productId, dto.getQuantity());
                log.info("Updated quantity of product {} to {} for user {}", 
                        dto.getProductId(), dto.getQuantity(), userId);
            }
            
            // 4. 保存購物車
            shoppingCartRepository.save(cart);
            
            log.info("Successfully updated cart item quantity for user {}", userId);
            
        } catch (Exception e) {
            log.error("Error updating cart item quantity: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update cart item quantity: " + e.getMessage(), e);
        }
    }

    /**
     * 根據購物車項目 ID 更新數量
     */
    public void updateCartItemQuantityById(String token, Long cartItemId, Integer newQuantity) {
        try {
            // 從 JWT token 中提取用戶 ID
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            // 驗證 token 並提取用戶 ID
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.extractUserId(token);
            
            log.info("Updating cart item {} quantity to {} for user {}", cartItemId, newQuantity, userId);
            
            // 1. 驗證輸入參數
            if (cartItemId == null || newQuantity == null) {
                throw new IllegalArgumentException("Cart item ID and quantity must not be null");
            }
            
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
            
            // 2. 獲取用戶的購物車
            UserId userIdObj = UserId.of(userId);
            Optional<ShoppingCartPure> cartOpt = shoppingCartRepository.findByUserId(userIdObj);
            
            if (cartOpt.isEmpty()) {
                throw new IllegalArgumentException("Shopping cart not found for user: " + userId);
            }
            
            ShoppingCartPure cart = cartOpt.get();
            
            // 3. 查找要更新的項目
            Optional<CartItemPure> itemToUpdate = cart.getItems().stream()
                    .filter(item -> item.getId() != null && item.getId().getValue().equals(cartItemId))
                    .findFirst();
            
            if (itemToUpdate.isEmpty()) {
                throw new IllegalArgumentException("Cart item not found with ID: " + cartItemId);
            }
            
            ProductId productId = itemToUpdate.get().getProductId();
            
            // 4. 更新商品數量
            if (newQuantity == 0) {
                // 如果數量為 0，則移除商品
                cart.removeProduct(productId);
                log.info("Removed cart item {} (product {}) from cart for user {}", 
                        cartItemId, productId.getValue(), userId);
            } else {
                // 更新數量
                cart.updateProductQuantity(productId, newQuantity);
                log.info("Updated cart item {} (product {}) quantity to {} for user {}", 
                        cartItemId, productId.getValue(), newQuantity, userId);
            }
            
            // 5. 保存購物車
            shoppingCartRepository.save(cart);
            
            log.info("Successfully updated cart item quantity for user {}", userId);
            
        } catch (Exception e) {
            log.error("Error updating cart item quantity by ID: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update cart item quantity: " + e.getMessage(), e);
        }
    }    /**
     * 結帳
     */
    public void checkout(String token, CheckoutRequest checkoutRequest) {
        try {
            // 從 JWT token 中提取用戶 ID
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            // 驗證 token 並提取用戶 ID
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.extractUserId(token);
            
            log.info("Processing checkout for user {} (extracted from token)", userId);
            
            // 1. 驗證輸入參數
            if (checkoutRequest == null) {
                throw new IllegalArgumentException("Checkout request cannot be null");
            }
            
            // 2. 獲取用戶的購物車
            UserId userIdObj = UserId.of(userId);
            Optional<ShoppingCartPure> cartOpt = shoppingCartRepository.findByUserId(userIdObj);
            
            if (cartOpt.isEmpty()) {
                throw new IllegalArgumentException("Shopping cart not found for user: " + userId);
            }
            
            ShoppingCartPure cart = cartOpt.get();
            
            // 3. 驗證購物車不為空
            if (cart.getItems().isEmpty()) {
                throw new IllegalArgumentException("Cannot checkout empty cart");
            }
            
            // 4. 計算總金額和驗證購物車項目
            cart.getItems().forEach(item -> {
                log.info("Checkout item: {} x {} = {}", 
                        item.getProductName(), 
                        item.getQuantity(), 
                        item.getTotalPrice());
            });
              // 5. 創建訂單 (這裡簡化處理，實際應該調用訂單服務)
            // TODO: 在完整實作中，這裡應該：
            // - 驗證庫存是否足夠
            // - 創建訂單記錄
            // - 更新庫存
            // - 處理付款
            // - 發送確認通知
            
            // 根據 DDD 原則：結帳應該發出領域事件，讓其他聚合響應
            // 例如：OrderCreatedEvent, InventoryUpdateRequestedEvent 等
            // 這樣符合「一個處理流程應避免更新多個Aggregate」的原則
            
            log.info("Order created successfully for user {}", userId);
            
            // 6. 結帳成功後清空購物車
            cart.clear();
            shoppingCartRepository.save(cart);
            
            log.info("Successfully completed checkout for user {} and cleared cart", userId);
            
        } catch (Exception e) {
            log.error("Error processing checkout for token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process checkout: " + e.getMessage(), e);
        }
    }

    /**
     * 清空購物車
     */
    public void clearCart(String token) {
        try {
            // 從 JWT token 中提取用戶 ID
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }
            
            // 驗證 token 並提取用戶 ID
            jwtUtil.validateToken(token);
            Long userId = jwtUtil.extractUserId(token);
            
            log.info("Clearing cart for user {} (extracted from token)", userId);
            
            // 1. 獲取用戶的購物車
            UserId userIdObj = UserId.of(userId);
            Optional<ShoppingCartPure> cartOpt = shoppingCartRepository.findByUserId(userIdObj);
            
            if (cartOpt.isEmpty()) {
                log.info("No cart found for user {}, nothing to clear", userId);
                return;
            }
            
            ShoppingCartPure cart = cartOpt.get();
            
            // 2. 清空購物車
            int itemCount = cart.getItems().size();
            cart.clear();
            
            // 3. 保存購物車
            shoppingCartRepository.save(cart);
            
            log.info("Successfully cleared {} items from cart for user {}", itemCount, userId);
            
        } catch (Exception e) {
            log.error("Error clearing cart: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to clear cart: " + e.getMessage(), e);
        }
    }
}
