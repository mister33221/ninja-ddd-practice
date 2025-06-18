package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories;

import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 產品 JPA Repository - 處理數據庫查詢
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT p FROM ProductEntity p WHERE p.status = 'PULL_ON_SHELVES'")
    List<ProductEntity> findAllActive();

    @Query("SELECT p FROM ProductEntity p WHERE p.category.id = :categoryId AND p.status = 'PULL_ON_SHELVES'")
    List<ProductEntity> findActiveByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM ProductEntity p WHERE p.stockQuantity > 0 AND p.status = 'PULL_ON_SHELVES'")
    List<ProductEntity> findAvailableProducts();

    Optional<ProductEntity> findByIdAndStatus(Long id, String status);
}
