package com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces;


import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;

import java.util.List;

public interface ProductRepository {


    List<Product> findByStatus(String status);


}
