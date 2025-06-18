package com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.ProductPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * 產品倉儲介面 - 定義純粹的領域操作
 * 不包含任何基礎設施技術細節
 */
public interface ProductRepository {
    
    Optional<ProductPure> findById(ProductId id);
    
    List<ProductPure> findAll();
    
    List<ProductPure> findAllActive();
    
    List<ProductPure> findByCategoryId(Long categoryId);
    
    List<ProductPure> findAvailableProducts();
    
    ProductPure save(ProductPure product);
    
    void deleteById(ProductId id);
    
    boolean existsById(ProductId id);
}
