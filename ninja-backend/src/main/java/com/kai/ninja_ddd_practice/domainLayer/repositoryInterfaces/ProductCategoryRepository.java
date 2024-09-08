package com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces;


import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory;

import java.util.Optional;

public interface ProductCategoryRepository {

    Optional<ProductCategory> findById(Long id);

}
