package com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductDetails;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.Money;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.StockQuantity;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.CartItemPure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("購物車聚合 - 領域邏輯測試")
class ShoppingCartPureTest {

    private ShoppingCartPure cart;
    private ProductPure testProduct;
    private UserId testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UserId.of(1L);
        cart = new ShoppingCartPure(null, testUserId);
        
        // 建立測試商品
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
    @DisplayName("當商品不存在於購物車時，應該新增商品項目")
    void should_add_new_item_when_product_not_exists() {
        // Given
        int quantity = 2;
        
        // When
        cart.addProduct(testProduct, quantity);
        
        // Then
        assertThat(cart.getItems()).hasSize(1);
        CartItemPure addedItem = cart.getItems().get(0);
        assertThat(addedItem.getProductId()).isEqualTo(testProduct.getId());
        assertThat(addedItem.getQuantity()).isEqualTo(quantity);
        assertThat(addedItem.getProductName()).isEqualTo("苦無");
        assertThat(addedItem.getUnitPrice()).isEqualTo(BigDecimal.valueOf(150));
    }

    @Test
    @DisplayName("當商品已存在於購物車時，應該累加數量")
    void should_increase_quantity_when_product_already_exists() {
        // Given
        cart.addProduct(testProduct, 2);
        
        // When
        cart.addProduct(testProduct, 3);
        
        // Then
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("當移除存在的商品時，應該成功移除")
    void should_remove_product_successfully_when_product_exists() {
        // Given
        cart.addProduct(testProduct, 2);
        assertThat(cart.getItems()).hasSize(1);
        
        // When
        cart.removeProduct(testProduct.getId());
        
        // Then
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    @DisplayName("當移除不存在的商品時，購物車應該保持不變")
    void should_remain_unchanged_when_removing_non_existent_product() {
        // Given
        cart.addProduct(testProduct, 2);
        ProductId nonExistentProductId = ProductId.of(999L);
        
        // When
        cart.removeProduct(nonExistentProductId);
        
        // Then
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getProductId()).isEqualTo(testProduct.getId());
    }

    @Test
    @DisplayName("當更新商品數量為0時，應該移除該商品")
    void should_remove_item_when_update_quantity_to_zero() {
        // Given
        cart.addProduct(testProduct, 2);
        
        // When
        cart.updateProductQuantity(testProduct.getId(), 0);
        
        // Then
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    @DisplayName("當更新商品數量為正數時，應該更新數量")
    void should_update_quantity_when_new_quantity_is_positive() {
        // Given
        cart.addProduct(testProduct, 2);
        
        // When
        cart.updateProductQuantity(testProduct.getId(), 5);
        
        // Then
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("當清空購物車時，所有商品都應該被移除")
    void should_clear_all_items_when_clearing_cart() {
        // Given
        ProductPure anotherProduct = ProductPure.builder()
            .id(ProductId.of(2L))
            .details(ProductDetails.builder()
                .name("手裏劍")
                .description("星型投擲武器")
                .build())
            .price(Money.of(BigDecimal.valueOf(80)))
            .stockQuantity(StockQuantity.of(200))
            .category(ProductCategory.builder().name("武器").build())
            .status("ACTIVE")
            .imageUrl("shuriken.jpg")
            .build();
            
        cart.addProduct(testProduct, 2);
        cart.addProduct(anotherProduct, 3);
        assertThat(cart.getItems()).hasSize(2);
        
        // When
        cart.clear();
        
        // Then
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    @DisplayName("添加商品數量為0時應該拋出異常")
    void should_throw_exception_when_adding_zero_quantity() {
        // When & Then
        assertThatThrownBy(() -> cart.addProduct(testProduct, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("數量必須大於 0");
    }

    @Test
    @DisplayName("添加商品數量為負數時應該拋出異常")
    void should_throw_exception_when_adding_negative_quantity() {
        // When & Then
        assertThatThrownBy(() -> cart.addProduct(testProduct, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("數量必須大於 0");
    }

    @Test
    @DisplayName("添加null商品時應該拋出異常")
    void should_throw_exception_when_adding_null_product() {
        // When & Then
        assertThatThrownBy(() -> cart.addProduct(null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("商品不能為空");
    }    @Test
    @DisplayName("購物車應該正確計算總金額")
    void should_calculate_total_amount_correctly() {
        // Given
        ProductPure product2 = ProductPure.builder()
                .id(ProductId.of(2L))
                .details(ProductDetails.builder()
                        .name("手裡劍")
                        .description("投擲武器")
                        .build())
                .price(Money.of(BigDecimal.valueOf(80)))
                .stockQuantity(StockQuantity.of(50))
                .category(ProductCategory.builder().name("武器").build())
                .status("ACTIVE")
                .imageUrl("shuriken.jpg")
                .build();

        // When
        cart.addProduct(testProduct, 2);  // 150 * 2 = 300
        cart.addProduct(product2, 3);     // 80 * 3 = 240        // Then
        assertThat(cart.getTotalAmount().getAmount()).isEqualByComparingTo(BigDecimal.valueOf(540));
    }@Test
    @DisplayName("更新商品數量為0時應該移除該項目_新版")
    void should_remove_item_when_update_quantity_to_zero_v2() {
        // Given
        cart.addProduct(testProduct, 2);
        assertThat(cart.getItems()).hasSize(1);

        // When
        cart.updateProductQuantity(ProductId.of(1L), 0);

        // Then
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    @DisplayName("更新不存在商品的數量時應該拋出異常")
    void should_throw_exception_when_update_non_existing_product() {
        // Given
        ProductId nonExistingProductId = ProductId.of(999L);        // When & Then
        assertThatThrownBy(() -> cart.updateProductQuantity(nonExistingProductId, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("商品不存在於購物車中");
    }

    @Test
    @DisplayName("移除不存在的商品時應該優雅處理")
    void should_handle_gracefully_when_removing_non_existing_product() {
        // Given
        ProductId nonExistingProductId = ProductId.of(999L);
        int originalSize = cart.getItems().size();

        // When & Then
        assertThatCode(() -> cart.removeProduct(nonExistingProductId))
                .doesNotThrowAnyException();
        
        assertThat(cart.getItems()).hasSize(originalSize);
    }

    @Test
    @DisplayName("購物車項目應該包含正確的商品資訊")
    void should_contain_correct_product_information_in_cart_item() {
        // Given & When
        cart.addProduct(testProduct, 3);

        // Then
        CartItemPure item = cart.getItems().get(0);
        assertThat(item.getProductId()).isEqualTo(ProductId.of(1L));
        assertThat(item.getProductName()).isEqualTo("苦無");
        assertThat(item.getQuantity()).isEqualTo(3);
        assertThat(item.getUnitPrice()).isEqualByComparingTo(BigDecimal.valueOf(150));
        assertThat(item.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(450));
    }

    @Test
    @DisplayName("清空購物車後應該沒有任何項目")
    void should_have_no_items_after_clear() {
        // Given
        cart.addProduct(testProduct, 2);
        assertThat(cart.getItems()).hasSize(1);

        // When
        cart.clear();

        // Then
        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getTotalAmount().getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("購物車應該支援多種相同商品的數量累加")
    void should_accumulate_quantity_for_same_product() {
        // Given & When
        cart.addProduct(testProduct, 2);
        cart.addProduct(testProduct, 3);

        // Then
        assertThat(cart.getItems()).hasSize(1);
        CartItemPure item = cart.getItems().get(0);
        assertThat(item.getQuantity()).isEqualTo(5);
        assertThat(item.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(750)); // 150 * 5
    }

    @Test
    @DisplayName("購物車項目ID應該在第一次添加時生成")
    void should_generate_item_id_when_first_added() {
        // Given & When
        cart.addProduct(testProduct, 1);

        // Then
        CartItemPure item = cart.getItems().get(0);
        assertThat(item.getId()).isNotNull();
    }

    @Test
    @DisplayName("檢查購物車是否為空")
    void should_check_if_cart_is_empty() {
        // Given
        assertThat(cart.isEmpty()).isTrue();

        // When
        cart.addProduct(testProduct, 1);

        // Then
        assertThat(cart.isEmpty()).isFalse();
    }    @Test
    @DisplayName("獲取購物車項目總數")
    void should_get_total_item_count() {
        // Given & When
        cart.addProduct(testProduct, 3);
        
        ProductPure product2 = ProductPure.builder()
                .id(ProductId.of(2L))
                .details(ProductDetails.builder().name("product2").build())
                .price(Money.of(BigDecimal.valueOf(100)))
                .stockQuantity(StockQuantity.of(50))
                .category(ProductCategory.builder().name("category").build())
                .status("ACTIVE")
                .imageUrl("test.jpg")
                .build();
        cart.addProduct(product2, 2);

        // Then - 檢查項目數量
        int totalItems = cart.getItems().stream()
                .mapToInt(item -> item.getQuantity())
                .sum();
        assertThat(totalItems).isEqualTo(5); // 3 + 2
    }
}
