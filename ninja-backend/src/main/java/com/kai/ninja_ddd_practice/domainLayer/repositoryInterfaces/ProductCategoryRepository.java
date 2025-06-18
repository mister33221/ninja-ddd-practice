package com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory;

import java.util.List;
import java.util.Optional;

/**
 * 產品分類倉儲介面 - 純粹的領域操作
 */
public interface ProductCategoryRepository {
    
    Optional<ProductCategory> findById(Long id);
    
    List<ProductCategory> findAll();
    
    List<ProductCategory> findAllActive();
    
    Optional<ProductCategory> findByName(String name);
    
    ProductCategory save(ProductCategory category);
    
    void deleteById(Long id);
    
    boolean existsByName(String name);
}