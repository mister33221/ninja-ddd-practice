package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories;

import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 產品分類 JPA Repository
 */
@Repository
public interface ProductCategoryJpaRepository extends JpaRepository<ProductCategoryEntity, Long> {

    @Query("SELECT c FROM ProductCategoryEntity c WHERE c.active = true")
    List<ProductCategoryEntity> findAllActive();

    Optional<ProductCategoryEntity> findByName(String name);

    boolean existsByName(String name);
}