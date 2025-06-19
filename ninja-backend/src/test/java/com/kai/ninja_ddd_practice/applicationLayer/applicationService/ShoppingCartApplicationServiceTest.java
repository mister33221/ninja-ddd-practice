package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductDetails;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.Money;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.StockQuantity;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCartPure;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ShoppingCartPureRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import com.kai.ninja_ddd_practice.infrastructureLayer.security.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("購物車應用服務 - 整合測試")
class ShoppingCartApplicationServiceTest {

    @Mock
    private ShoppingCartPureRepository shoppingCartRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private ShoppingCartApplicationService shoppingCartService;

    private String validToken;
    private Long userId;
    private AddToCartDto addToCartDto;
    private ProductPure testProduct;

    @BeforeEach
    void setUp() {
        validToken = "valid.jwt.token";
        userId = 1L;
        addToCartDto = AddToCartDto.builder()
            .productId(1L)
            .quantity(2)
            .build();
            
        testProduct = ProductPure.builder()
            .id(ProductId.of(1L))
            .details(ProductDetails.builder()
                .name("苦無")
                .description("基本忍具")
                .build())
            .price(Money.of(BigDecimal.valueOf(150)))
            .stockQuantity(StockQuantity.of(100))
            .category(ProductCategory.builder().name("武器").build())
            .status("ACTIVE")
            .imageUrl("kunai.jpg")
            .build();
    }    @Test
    @DisplayName("當用戶沒有購物車時，應該創建新購物車並添加商品")
    void should_create_new_cart_when_user_has_no_cart() {
        // Given
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUserId(validToken)).thenReturn(userId);
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.of(testProduct));
        when(shoppingCartRepository.findByUserId(UserId.of(userId))).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.addToCart(validToken, addToCartDto);
        
        // Then
        verify(jwtUtil).validateToken(validToken);
        verify(jwtUtil).extractUserId(validToken);
        verify(productRepository).findById(ProductId.of(1L));
        verify(shoppingCartRepository).findByUserId(UserId.of(userId));
        verify(shoppingCartRepository).save(any(ShoppingCartPure.class));
    }    @Test
    @DisplayName("當用戶已有購物車時，應該在現有購物車中添加商品")
    void should_add_to_existing_cart_when_user_has_cart() {
        // Given
        ShoppingCartPure existingCart = new ShoppingCartPure(null, UserId.of(userId));
        
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUserId(validToken)).thenReturn(userId);
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.of(testProduct));
        when(shoppingCartRepository.findByUserId(UserId.of(userId))).thenReturn(Optional.of(existingCart));
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.addToCart(validToken, addToCartDto);
        
        // Then
        verify(shoppingCartRepository).save(existingCart);
        assertThat(existingCart.getItems()).hasSize(1);
        assertThat(existingCart.getItems().get(0).getQuantity()).isEqualTo(2);
    }    @Test
    @DisplayName("當Token為空時，應該拋出異常")
    void should_throw_exception_when_token_is_null() {
        // When & Then
        assertThatThrownBy(() -> shoppingCartService.addToCart(null, addToCartDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Failed to add product to cart: Token cannot be null or empty");
            
        verify(jwtUtil, never()).validateToken(any());
        verify(productRepository, never()).findById(any());
        verify(shoppingCartRepository, never()).save(any());
    }    @Test
    @DisplayName("當Token為空字符串時，應該拋出異常")
    void should_throw_exception_when_token_is_empty() {
        // When & Then
        assertThatThrownBy(() -> shoppingCartService.addToCart("", addToCartDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Failed to add product to cart: Token cannot be null or empty");
            
        verify(jwtUtil, never()).validateToken(any());
        verify(productRepository, never()).findById(any());
        verify(shoppingCartRepository, never()).save(any());
    }    @Test
    @DisplayName("當JWT無效時，應該拋出異常")
    void should_throw_exception_when_jwt_invalid() {
        // Given
        doThrow(new IllegalArgumentException("Invalid JWT"))
            .when(jwtUtil).validateToken(validToken);
        
        // When & Then
        assertThatThrownBy(() -> shoppingCartService.addToCart(validToken, addToCartDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Failed to add product to cart");
            
        verify(jwtUtil).validateToken(validToken);
        verify(jwtUtil, never()).extractUserId(any());
        verify(productRepository, never()).findById(any());
        verify(shoppingCartRepository, never()).save(any());
    }    @Test
    @DisplayName("當商品不存在時，應該拋出異常")
    void should_throw_exception_when_product_not_found() {
        // Given
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUserId(validToken)).thenReturn(userId);
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> shoppingCartService.addToCart(validToken, addToCartDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Failed to add product to cart");
            
        verify(shoppingCartRepository, never()).save(any());
    }    @Test
    @DisplayName("當DTO為空時，應該拋出異常")
    void should_throw_exception_when_dto_is_null() {
        // When & Then
        assertThatThrownBy(() -> shoppingCartService.addToCart(validToken, null))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Failed to add product to cart");
            
        verify(productRepository, never()).findById(any());
        verify(shoppingCartRepository, never()).save(any());
    }@Test
    @DisplayName("當商品ID為空時，應該拋出異常")
    void should_throw_exception_when_product_id_is_null() {
        // Given
        AddToCartDto invalidDto = AddToCartDto.builder()
            .productId(null)
            .quantity(1)
            .build();
            
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUserId(validToken)).thenReturn(userId);
        
        // When & Then
        assertThatThrownBy(() -> shoppingCartService.addToCart(validToken, invalidDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Failed to add product to cart");
            
        verify(productRepository, never()).findById(any());
        verify(shoppingCartRepository, never()).save(any());
    }    @Test
    @DisplayName("當數量為空時，應該拋出異常")
    void should_throw_exception_when_quantity_is_null() {
        // Given
        AddToCartDto invalidDto = AddToCartDto.builder()
            .productId(1L)
            .quantity(null)
            .build();
            
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUserId(validToken)).thenReturn(userId);
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.of(testProduct));
        when(shoppingCartRepository.findByUserId(UserId.of(userId))).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When & Then - quantity 為 null 時，系統會使用默認值 1
        shoppingCartService.addToCart(validToken, invalidDto);
        
        verify(productRepository).findById(ProductId.of(1L));
        verify(shoppingCartRepository).save(any(ShoppingCartPure.class));
    }    @Test
    @DisplayName("當數量小於1時，應該拋出異常")
    void should_throw_exception_when_quantity_is_less_than_one() {
        // Given
        AddToCartDto invalidDto = AddToCartDto.builder()
            .productId(1L)
            .quantity(0)
            .build();
            
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUserId(validToken)).thenReturn(userId);
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.of(testProduct));
        when(shoppingCartRepository.findByUserId(UserId.of(userId))).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When & Then - quantity 為 0 時，系統會使用默認值 1
        shoppingCartService.addToCart(validToken, invalidDto);
        
        verify(productRepository).findById(ProductId.of(1L));
        verify(shoppingCartRepository).save(any(ShoppingCartPure.class));
    }    @Test
    @DisplayName("當相同商品重複添加時，應該累加數量")
    void should_accumulate_quantity_when_adding_same_product_multiple_times() {
        // Given
        ShoppingCartPure existingCart = new ShoppingCartPure(null, UserId.of(userId));
        existingCart.addProduct(testProduct, 3); // 先添加3個
        
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUserId(validToken)).thenReturn(userId);
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.of(testProduct));
        when(shoppingCartRepository.findByUserId(UserId.of(userId))).thenReturn(Optional.of(existingCart));
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.addToCart(validToken, addToCartDto); // 再添加2個
        
        // Then
        assertThat(existingCart.getItems()).hasSize(1);
        assertThat(existingCart.getItems().get(0).getQuantity()).isEqualTo(5); // 3 + 2 = 5
    }    @Test
    @DisplayName("移除商品時應該成功")
    void should_remove_product_successfully() {
        // Given
        ShoppingCartPure existingCart = new ShoppingCartPure(null, UserId.of(userId));
        existingCart.addProduct(testProduct, 2);
        
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUserId(validToken)).thenReturn(userId);
        when(shoppingCartRepository.findByUserId(UserId.of(userId))).thenReturn(Optional.of(existingCart));
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.removeFromCart(validToken, 1L);
        
        // Then
        assertThat(existingCart.getItems()).isEmpty();
        verify(shoppingCartRepository).save(existingCart);
    }

    @Test
    @DisplayName("清空購物車時應該成功")
    void should_clear_cart_successfully() {
        // Given
        ShoppingCartPure existingCart = new ShoppingCartPure(null, UserId.of(userId));
        existingCart.addProduct(testProduct, 2);
        
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUserId(validToken)).thenReturn(userId);
        when(shoppingCartRepository.findByUserId(UserId.of(userId))).thenReturn(Optional.of(existingCart));
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.clearCart(validToken);
        
        // Then
        assertThat(existingCart.getItems()).isEmpty();
        verify(shoppingCartRepository).save(existingCart);
    }
}
