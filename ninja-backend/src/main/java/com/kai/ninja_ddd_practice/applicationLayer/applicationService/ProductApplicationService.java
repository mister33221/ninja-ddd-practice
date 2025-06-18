package com.kai.ninja_ddd_practice.applicationLayer.applicationService;

import com.kai.ninja_ddd_practice.applicationLayer.dtos.GetProductCardsDto;
import com.kai.ninja_ddd_practice.applicationLayer.mappers.ProductApplicationLayerMapper;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.*;
import com.kai.ninja_ddd_practice.domainLayer.domainEvents.ProductStockChangedEvent;
import com.kai.ninja_ddd_practice.domainLayer.domainServices.DomainEventPublisher;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 產品應用服務 - 使用純淨的防腐層架構
 * 展示如何正確使用防腐層進行業務操作
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductApplicationService {

    private final ProductRepository productRepository; // 防腐層 Repository
    private final DomainEventPublisher eventPublisher; // 防腐層事件發布者

    /**
     * 獲取產品卡片列表 (原有功能保持向後兼容)
     */
    @Transactional(readOnly = true)
    public List<GetProductCardsDto> getProductCards() {
        log.info("Fetching product cards");
        
        List<ProductPure> products = productRepository.findAvailableProducts();
        
        return products.stream()
                .map(ProductApplicationLayerMapper::covertProductToGetProductCardsDto)
                .toList();
    }

    /**
     * 獲取所有有效產品
     */
    @Transactional(readOnly = true)
    public List<ProductPure> getAllActiveProducts() {
        log.info("Fetching all active products");
        return productRepository.findAllActive();
    }

    /**
     * 根據ID獲取產品
     */
    @Transactional(readOnly = true)
    public ProductPure getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        
        ProductId productId = ProductId.of(id);
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * 創建新產品
     */
    @Transactional
    public ProductPure createProduct(String name, String description, BigDecimal price, 
                                   int stockQuantity, Long categoryId) {
        log.info("Creating new product: {}", name);

        // 1. 創建值對象
        ProductDetails details = ProductDetails.builder()
                .name(name)
                .description(description)
                .build();

        Money productPrice = Money.of(price);
        StockQuantity stock = StockQuantity.of(stockQuantity);

        // 2. 創建產品聚合根（純淨的領域模型）
        ProductPure product = ProductPure.builder()
                .id(null) // 新產品，ID 由數據庫生成
                .details(details)
                .price(productPrice)
                .stockQuantity(stock)
                .status("ACTIVE")
                .imageUrl("https://example.com/default-product.jpg")
                .build();

        // 3. 通過防腐層保存到數據庫
        ProductPure savedProduct = productRepository.save(product);

        log.info("Product created successfully with id: {}", savedProduct.getId().getValue());
        return savedProduct;
    }

    /**
     * 更新產品庫存
     */
    @Transactional
    public void updateProductStock(Long productId, int quantityChange, String reason) {
        log.info("Updating stock for product {} by {} units. Reason: {}", 
                productId, quantityChange, reason);

        // 1. 獲取產品（通過防腐層）
        ProductId id = ProductId.of(productId);
        ProductPure product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. 記錄變更前的庫存
        int oldStock = product.getStockQuantity().getValue();

        // 3. 執行業務邏輯（純粹的領域邏輯）
        product.updateStock(quantityChange);

        // 4. 保存變更（通過防腐層）
        productRepository.save(product);

        // 5. 發布領域事件（通過防腐層）
        ProductStockChangedEvent event = ProductStockChangedEvent.create(
                productId.toString(),
                oldStock,
                product.getStockQuantity().getValue(),
                reason
        );
        eventPublisher.publish(event);

        log.info("Stock updated successfully. Old: {}, New: {}", 
                oldStock, product.getStockQuantity().getValue());
    }

    /**
     * 更新產品價格
     */
    @Transactional
    public void updateProductPrice(Long productId, BigDecimal newPrice) {
        log.info("Updating price for product {} to {}", productId, newPrice);

        // 1. 獲取產品
        ProductId id = ProductId.of(productId);
        ProductPure product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. 執行業務邏輯
        Money newMoney = Money.of(newPrice);
        product.updatePrice(newMoney);

        // 3. 保存變更
        productRepository.save(product);

        log.info("Price updated successfully for product {}", productId);
    }

    /**
     * 檢查產品可用性
     */
    @Transactional(readOnly = true)
    public boolean isProductAvailable(Long productId, int requestedQuantity) {
        ProductId id = ProductId.of(productId);
        ProductPure product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return product.isAvailable() && product.hasEnoughStock(requestedQuantity);
    }

    /**
     * 減少產品庫存（用於訂單處理）
     */
    @Transactional
    public void reserveProductStock(Long productId, int quantity) {
        log.info("Reserving {} units of product {}", quantity, productId);

        ProductId id = ProductId.of(productId);
        ProductPure product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 業務邏輯：檢查並減少庫存
        product.reduceStock(quantity);

        // 保存變更
        productRepository.save(product);

        // 發布庫存變更事件
        ProductStockChangedEvent event = ProductStockChangedEvent.create(
                productId.toString(),
                product.getStockQuantity().getValue() + quantity,
                product.getStockQuantity().getValue(),
                "Stock reserved for order"
        );
        eventPublisher.publish(event);

        log.info("Stock reserved successfully for product {}", productId);
    }
}
