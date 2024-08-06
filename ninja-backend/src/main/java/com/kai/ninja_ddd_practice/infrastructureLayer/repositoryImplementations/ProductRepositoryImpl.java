package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.aggregateRoot.Product;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryImpl extends JpaRepository<Product, Long>, ProductRepository {
}
