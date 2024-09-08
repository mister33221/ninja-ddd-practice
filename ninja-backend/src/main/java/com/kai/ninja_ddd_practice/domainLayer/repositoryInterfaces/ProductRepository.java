package com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces;


import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {


    Optional<List<Product>> findByStatus(String pullOnShelves);

    Optional<Product> findById(Long productId);
}
