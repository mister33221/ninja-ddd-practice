package com.kai.ninja_ddd_practice.infrastructureLayer.repositoryImplementations;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.productCategpry.aggregateRoot.ProductCategory;
import com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces.ProductCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepositoryImpl extends JpaRepository<ProductCategory, Long>, ProductCategoryRepository {
}
