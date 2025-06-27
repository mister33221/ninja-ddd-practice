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
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.ShoppingCartId;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ShoppingCartPureRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
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
class ShoppingCartApplicationServiceTest_New {

    @Mock
    private ShoppingCartPureRepository shoppingCartRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ShoppingCartApplicationService shoppingCartService;

    private UserId userId;
    private AddToCartDto addToCartDto;
    private ProductPure testProduct;

    @BeforeEach
    void setUp() {
        userId = UserId.of(1L);
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
    }

    @Test
    @DisplayName("當用戶沒有購物車時，應該創建新購物車並添加商品")
    void should_create_new_cart_when_user_has_no_cart() {
        // Given
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.of(testProduct));
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.addToCart(userId, addToCartDto);
        
        // Then
        verify(productRepository).findById(ProductId.of(1L));
        verify(shoppingCartRepository).findByUserId(userId);
        verify(shoppingCartRepository).save(any(ShoppingCartPure.class));
    }    @Test
    @DisplayName("當用戶已有購物車時，應該添加商品到現有購物車")
    void should_add_product_to_existing_cart() {
        // Given
        ShoppingCartPure existingCart = new ShoppingCartPure(ShoppingCartId.of(1L), userId);
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.of(testProduct));
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.addToCart(userId, addToCartDto);
        
        // Then
        verify(productRepository).findById(ProductId.of(1L));
        verify(shoppingCartRepository).findByUserId(userId);
        verify(shoppingCartRepository).save(any(ShoppingCartPure.class));
    }

    @Test
    @DisplayName("當 UserId 為 null 時，應該拋出例外")
    void should_throw_exception_when_userId_is_null() {
        // When & Then
        assertThatThrownBy(() -> shoppingCartService.addToCart(null, addToCartDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("UserId cannot be null");
    }

    @Test
    @DisplayName("當商品不存在時，應該拋出例外")
    void should_throw_exception_when_product_not_found() {
        // Given
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> shoppingCartService.addToCart(userId, addToCartDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Product not found");
    }

    @Test
    @DisplayName("當 DTO 為 null 時，應該拋出例外")
    void should_throw_exception_when_dto_is_null() {
        // When & Then
        assertThatThrownBy(() -> shoppingCartService.addToCart(userId, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("AddToCartDto cannot be null");
    }

    @Test
    @DisplayName("當商品ID為null時，應該拋出例外")
    void should_throw_exception_when_product_id_is_null() {
        // Given
        AddToCartDto invalidDto = AddToCartDto.builder()
            .productId(null)
            .quantity(1)
            .build();
        
        // When & Then
        assertThatThrownBy(() -> shoppingCartService.addToCart(userId, invalidDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product ID must not be null");
    }

    @Test
    @DisplayName("當數量為null時，應該使用默認數量1")
    void should_use_default_quantity_when_quantity_is_null() {
        // Given
        AddToCartDto dtoWithNullQuantity = AddToCartDto.builder()
            .productId(1L)
            .quantity(null)
            .build();
        
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.of(testProduct));
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.addToCart(userId, dtoWithNullQuantity);
        
        // Then
        verify(shoppingCartRepository).save(any(ShoppingCartPure.class));
    }

    @Test
    @DisplayName("當數量為0或負數時，應該使用默認數量1")
    void should_use_default_quantity_when_quantity_is_zero_or_negative() {
        // Given
        AddToCartDto dtoWithZeroQuantity = AddToCartDto.builder()
            .productId(1L)
            .quantity(0)
            .build();
        
        when(productRepository.findById(ProductId.of(1L))).thenReturn(Optional.of(testProduct));
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.addToCart(userId, dtoWithZeroQuantity);
        
        // Then
        verify(shoppingCartRepository).save(any(ShoppingCartPure.class));
    }    @Test
    @DisplayName("應該能夠從購物車移除商品")
    void should_remove_product_from_cart() {
        // Given
        ShoppingCartPure existingCart = new ShoppingCartPure(ShoppingCartId.of(1L), userId);
        existingCart.addProduct(testProduct, 1);
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.removeFromCart(userId, 1L);
        
        // Then
        verify(shoppingCartRepository).findByUserId(userId);
        verify(shoppingCartRepository).save(any(ShoppingCartPure.class));
    }    @Test
    @DisplayName("應該能夠清空購物車")
    void should_clear_cart() {
        // Given
        ShoppingCartPure existingCart = new ShoppingCartPure(ShoppingCartId.of(1L), userId);
        existingCart.addProduct(testProduct, 1);
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));
        when(shoppingCartRepository.save(any(ShoppingCartPure.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        shoppingCartService.clearCart(userId);
        
        // Then
        verify(shoppingCartRepository).findByUserId(userId);
        verify(shoppingCartRepository).save(any(ShoppingCartPure.class));
    }
}
