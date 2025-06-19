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
    @DisplayName("當商品數量為負數時，應該拋出異常")
    void should_throw_exception_when_adding_negative_quantity() {
        // Given
        int negativeQuantity = -1;
        
        // When & Then
        assertThatThrownBy(() -> cart.addProduct(testProduct, negativeQuantity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Product and quantity must be valid");
    }

    @Test
    @DisplayName("當商品為空時，應該拋出異常")
    void should_throw_exception_when_adding_null_product() {
        // When & Then
        assertThatThrownBy(() -> cart.addProduct(null, 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Product and quantity must be valid");
    }

    @Test
    @DisplayName("計算購物車總金額應該正確")
    void should_calculate_total_amount_correctly() {
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
            
        cart.addProduct(testProduct, 2);    // 150 * 2 = 300
        cart.addProduct(anotherProduct, 3); // 80 * 3 = 240
        
        // When
        Money totalAmount = cart.getTotalAmount();
        
        // Then
        assertThat(totalAmount.getAmount()).isEqualTo(BigDecimal.valueOf(540));
        assertThat(totalAmount.getCurrency()).isEqualTo("TWD");
    }

    @Test
    @DisplayName("計算購物車商品總數量應該正確")
    void should_calculate_total_item_count_correctly() {
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
        
        // When
        Integer totalCount = cart.getItemCount();
        
        // Then
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    @DisplayName("空購物車應該回傳isEmpty為true")
    void should_return_true_when_cart_is_empty() {
        // When & Then
        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("有商品的購物車應該回傳isEmpty為false")
    void should_return_false_when_cart_has_items() {
        // Given
        cart.addProduct(testProduct, 1);
        
        // When & Then
        assertThat(cart.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("創建新購物車的靜態方法應該正常運作")
    void should_create_new_cart_with_static_method() {
        // When
        ShoppingCartPure newCart = ShoppingCartPure.createNewCart(testUserId);
        
        // Then
        assertThat(newCart.getUserId()).isEqualTo(testUserId);
        assertThat(newCart.getItems()).isEmpty();
        assertThat(newCart.getId()).isNull(); // ID will be generated when persisted
    }
}
