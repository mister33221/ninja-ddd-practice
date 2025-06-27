package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCartPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.ShoppingCartId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductDetails;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.Money;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.StockQuantity;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ShoppingCartEntity;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.mappers.ShoppingCartEntityMapper;
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories.ShoppingCartJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("購物車Repository實現 - 單元測試")
class ShoppingCartPureRepositoryImplTest {    @Mock
    private ShoppingCartJpaRepository shoppingCartJpaRepository;

    @Mock
    private ShoppingCartEntityMapper mapper;

    @InjectMocks
    private ShoppingCartPureRepositoryImpl shoppingCartRepository;

    private UserId testUserId;
    private ShoppingCartPure testCart;
    private ShoppingCartEntity testEntity;
    private ProductPure testProduct;

    @BeforeEach
    void setUp() {
        testUserId = UserId.of(1L);
        
        testProduct = ProductPure.builder()
                .id(ProductId.of(1L))
                .details(ProductDetails.builder()
                        .name("測試商品")
                        .description("測試描述")
                        .build())
                .price(Money.of(BigDecimal.valueOf(100)))
                .stockQuantity(StockQuantity.of(50))
                .category(ProductCategory.builder().name("測試分類").build())
                .status("ACTIVE")
                .imageUrl("test.jpg")
                .build();

        testCart = new ShoppingCartPure(ShoppingCartId.of(1L), testUserId);
        testCart.addProduct(testProduct, 2);

        testEntity = new ShoppingCartEntity();
        testEntity.setId(1L);
        testEntity.setUserId(1L);
    }    @Test
    @DisplayName("根據用戶ID查找購物車 - 存在時返回")
    void should_find_cart_by_user_id_when_exists() {
        // Given
        when(shoppingCartJpaRepository.findByUserIdWithItems(1L)).thenReturn(Optional.of(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testCart);

        // When
        Optional<ShoppingCartPure> result = shoppingCartRepository.findByUserId(testUserId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId().getValue());
        verify(shoppingCartJpaRepository).findByUserIdWithItems(1L);
        verify(mapper).toDomain(testEntity);
    }    @Test
    @DisplayName("根據用戶ID查找購物車 - 不存在時返回空")
    void should_return_empty_when_cart_not_exists() {
        // Given
        when(shoppingCartJpaRepository.findByUserIdWithItems(1L)).thenReturn(Optional.empty());

        // When
        Optional<ShoppingCartPure> result = shoppingCartRepository.findByUserId(testUserId);

        // Then
        assertFalse(result.isPresent());
        verify(shoppingCartJpaRepository).findByUserIdWithItems(1L);
    }@Test
    @DisplayName("保存購物車 - 新購物車（ID為null）")
    void should_save_new_cart_when_id_is_null() {
        // Given
        ShoppingCartPure newCart = new ShoppingCartPure(null, testUserId);
        ShoppingCartEntity savedEntity = new ShoppingCartEntity();
        savedEntity.setId(2L);
        savedEntity.setUserId(1L);
        
        ShoppingCartPure expectedResult = new ShoppingCartPure(ShoppingCartId.of(2L), testUserId);

        when(mapper.toEntity(newCart)).thenReturn(new ShoppingCartEntity());
        when(shoppingCartJpaRepository.save(any(ShoppingCartEntity.class))).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(expectedResult);

        // When
        ShoppingCartPure result = shoppingCartRepository.save(newCart);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId().getValue());
        verify(mapper).toEntity(newCart);
        verify(shoppingCartJpaRepository).save(any(ShoppingCartEntity.class));
        verify(mapper).toDomain(savedEntity);
    }    @Test
    @DisplayName("保存購物車 - 更新現有購物車")
    void should_update_existing_cart() {
        // Given
        when(mapper.toEntity(testCart)).thenReturn(testEntity);
        when(shoppingCartJpaRepository.save(any(ShoppingCartEntity.class))).thenReturn(testEntity);
        when(mapper.toDomain(testEntity)).thenReturn(testCart);

        // When
        ShoppingCartPure result = shoppingCartRepository.save(testCart);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId().getValue());
        verify(mapper).toEntity(testCart);
        verify(shoppingCartJpaRepository).save(any(ShoppingCartEntity.class));
        verify(mapper).toDomain(testEntity);
    }

    @Test
    @DisplayName("根據ID查找購物車 - 存在時返回")
    void should_find_cart_by_id_when_exists() {        // Given
        ShoppingCartId cartId = ShoppingCartId.of(1L);
        when(shoppingCartJpaRepository.findByIdWithItems(1L)).thenReturn(Optional.of(testEntity));
        when(mapper.toDomain(testEntity)).thenReturn(testCart);

        // When
        Optional<ShoppingCartPure> result = shoppingCartRepository.findById(cartId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId().getValue());
        verify(shoppingCartJpaRepository).findByIdWithItems(1L);
        verify(mapper).toDomain(testEntity);
    }    @Test
    @DisplayName("根據ID查找購物車 - 不存在時返回空")
    void should_return_empty_when_cart_id_not_exists() {
        // Given
        ShoppingCartId cartId = ShoppingCartId.of(999L);
        when(shoppingCartJpaRepository.findByIdWithItems(999L)).thenReturn(Optional.empty());

        // When
        Optional<ShoppingCartPure> result = shoppingCartRepository.findById(cartId);

        // Then
        assertFalse(result.isPresent());
        verify(shoppingCartJpaRepository).findByIdWithItems(999L);
    }

    @Test
    @DisplayName("刪除購物車 - 成功")
    void should_delete_cart_successfully() {
        // Given
        doNothing().when(shoppingCartJpaRepository).deleteById(1L);

        // When
        assertDoesNotThrow(() -> shoppingCartRepository.deleteById(ShoppingCartId.of(1L)));

        // Then
        verify(shoppingCartJpaRepository).deleteById(1L);
    }
}
