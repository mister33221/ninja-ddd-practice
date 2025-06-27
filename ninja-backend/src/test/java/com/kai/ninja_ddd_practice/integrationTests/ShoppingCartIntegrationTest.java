package com.kai.ninja_ddd_practice.integrationTests;

import com.kai.ninja_ddd_practice.applicationLayer.applicationService.ShoppingCartApplicationService;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.AddToCartDto;
import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetShoppingCartDto;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductDetails;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.Money;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.StockQuantity;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductCategoryRepository;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ShoppingCartPureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
@DisplayName("購物車功能 - 整合測試")
class ShoppingCartIntegrationTest {

    @Autowired
    private ShoppingCartApplicationService shoppingCartService;

    @Autowired
    private ShoppingCartPureRepository shoppingCartRepository;    
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private UserId testUserId;
    private ProductPure testProduct;

    @BeforeEach
    void setUp() {
        testUserId = UserId.of(1L);        
        // 使用現有的產品分類和產品（從 data.sql）
        // 產品分類 ID=1 (Electronics) 已存在
        // 產品 ID=9999 (Kunai) 已存在，價格 10.99
        testProduct = ProductPure.builder()
                .id(ProductId.of(9999L))
                .details(ProductDetails.builder()
                        .name("Kunai")
                        .description("A small throwing knife used by ninjas")
                        .build())
                .price(Money.of(BigDecimal.valueOf(10.99)))
                .stockQuantity(StockQuantity.of(100))
                .category(ProductCategory.builder()
                        .id(1L)
                        .name("Electronics")
                        .description("Electronics category")
                        .active(true)
                        .build())
                .status("PULL_ON_SHELVES")
                .imageUrl("kunai.jpg")
                .build();
    }

    @Test
    @DisplayName("完整的購物車流程 - 添加、查詢、更新、清空")
    void should_complete_full_shopping_cart_workflow() {
        // 1. 初始狀態 - 購物車應該是空的
        GetShoppingCartDto emptyCart = shoppingCartService.getShoppingCart(testUserId);
        assertNotNull(emptyCart);
        assertEquals(0, emptyCart.getCartItems().length);        // 2. 添加商品到購物車 (使用現有商品 ID=9999)
        AddToCartDto addToCartDto = AddToCartDto.builder()
                .productId(9999L)
                .quantity(3)
                .build();

        assertDoesNotThrow(() -> shoppingCartService.addToCart(testUserId, addToCartDto));

        // 3. 驗證商品已添加
        GetShoppingCartDto cartWithItems = shoppingCartService.getShoppingCart(testUserId);
        assertNotNull(cartWithItems);
        assertEquals(1, cartWithItems.getCartItems().length);
        assertEquals(3, cartWithItems.getCartItems()[0].getQuantity());
        assertEquals("Kunai", cartWithItems.getCartItems()[0].getProductName());

        // 4. 再次添加相同商品（應該累加數量）
        assertDoesNotThrow(() -> shoppingCartService.addToCart(testUserId, addToCartDto));

        GetShoppingCartDto cartAfterSecondAdd = shoppingCartService.getShoppingCart(testUserId);
        assertEquals(1, cartAfterSecondAdd.getCartItems().length);
        assertEquals(6, cartAfterSecondAdd.getCartItems()[0].getQuantity()); // 3 + 3 = 6        // 5. 移除商品
        assertDoesNotThrow(() -> shoppingCartService.removeFromCart(testUserId, 9999L));

        // 6. 驗證購物車已清空
        GetShoppingCartDto cartAfterRemove = shoppingCartService.getShoppingCart(testUserId);
        assertEquals(0, cartAfterRemove.getCartItems().length);
    }

    @Test
    @DisplayName("清空購物車功能測試")
    void should_clear_cart_successfully() {        // Given - 添加商品到購物車
        AddToCartDto addToCartDto = AddToCartDto.builder()
                .productId(9999L)
                .quantity(2)
                .build();
        shoppingCartService.addToCart(testUserId, addToCartDto);

        // 驗證購物車有商品
        GetShoppingCartDto cartWithItems = shoppingCartService.getShoppingCart(testUserId);
        assertEquals(1, cartWithItems.getCartItems().length);

        // When - 清空購物車
        assertDoesNotThrow(() -> shoppingCartService.clearCart(testUserId));

        // Then - 驗證購物車已清空
        GetShoppingCartDto emptyCart = shoppingCartService.getShoppingCart(testUserId);
        assertEquals(0, emptyCart.getCartItems().length);
    }    @Test
    @DisplayName("多用戶購物車隔離測試")
    void should_isolate_carts_between_different_users() {
        // Given
        UserId user1 = UserId.of(1L);
        UserId user2 = UserId.of(2L);

        AddToCartDto addToCartDto = AddToCartDto.builder()
                .productId(9999L)
                .quantity(2)
                .build();

        // When - 用戶1添加商品
        shoppingCartService.addToCart(user1, addToCartDto);

        // Then - 用戶1有商品，用戶2沒有商品
        GetShoppingCartDto user1Cart = shoppingCartService.getShoppingCart(user1);
        GetShoppingCartDto user2Cart = shoppingCartService.getShoppingCart(user2);

        assertEquals(1, user1Cart.getCartItems().length);
        assertEquals(0, user2Cart.getCartItems().length);        // When - 用戶2添加商品
        AddToCartDto user2AddDto = AddToCartDto.builder()
                .productId(9999L)
                .quantity(5)
                .build();
        shoppingCartService.addToCart(user2, user2AddDto);

        // Then - 兩個用戶都有各自的商品
        user1Cart = shoppingCartService.getShoppingCart(user1);
        user2Cart = shoppingCartService.getShoppingCart(user2);

        assertEquals(1, user1Cart.getCartItems().length);
        assertEquals(2, user1Cart.getCartItems()[0].getQuantity());

        assertEquals(1, user2Cart.getCartItems().length);
        assertEquals(5, user2Cart.getCartItems()[0].getQuantity());
    }

    @Test
    @DisplayName("添加不存在商品時應該拋出異常")
    void should_throw_exception_when_adding_non_existing_product() {
        // Given
        AddToCartDto invalidDto = AddToCartDto.builder()
                .productId(999999999L) // 不存在的商品ID
                .quantity(1)
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> shoppingCartService.addToCart(testUserId, invalidDto));
    }

    @Test
    @DisplayName("購物車應該持久化到資料庫")
    void should_persist_cart_to_database() {        // Given
        AddToCartDto addToCartDto = AddToCartDto.builder()
                .productId(9999L)
                .quantity(2)
                .build();

        // When
        shoppingCartService.addToCart(testUserId, addToCartDto);

        // Then - 直接從Repository查詢驗證
        Optional<com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCartPure> savedCart = 
                shoppingCartRepository.findByUserId(testUserId);

        assertTrue(savedCart.isPresent());
        assertEquals(1, savedCart.get().getItems().size());
        assertEquals(2, savedCart.get().getItems().get(0).getQuantity());
    }
}
