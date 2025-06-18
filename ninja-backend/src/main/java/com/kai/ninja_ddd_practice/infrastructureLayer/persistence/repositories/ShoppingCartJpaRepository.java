package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories;

import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 購物車 JPA Repository
 */
@Repository
public interface ShoppingCartJpaRepository extends JpaRepository<ShoppingCartEntity, Long> {

    /**
     * 根據用戶 ID 查找購物車
     */
    Optional<ShoppingCartEntity> findByUserId(Long userId);

    /**
     * 根據用戶 ID 刪除購物車
     */
    void deleteByUserId(Long userId);

    /**
     * 檢查用戶是否有購物車
     */
    boolean existsByUserId(Long userId);

    /**
     * 根據用戶 ID 列表查找購物車
     */
    List<ShoppingCartEntity> findByUserIdIn(List<Long> userIds);

    /**
     * 查找有商品的購物車（包含項目）
     */
    @Query("SELECT DISTINCT sc FROM ShoppingCartEntity sc LEFT JOIN FETCH sc.items WHERE sc.id = :id")
    Optional<ShoppingCartEntity> findByIdWithItems(@Param("id") Long id);

    /**
     * 查找用戶的購物車（包含項目）
     */
    @Query("SELECT DISTINCT sc FROM ShoppingCartEntity sc LEFT JOIN FETCH sc.items WHERE sc.userId = :userId")
    Optional<ShoppingCartEntity> findByUserIdWithItems(@Param("userId") Long userId);

    /**
     * 查找所有購物車（包含項目）
     */
    @Query("SELECT DISTINCT sc FROM ShoppingCartEntity sc LEFT JOIN FETCH sc.items")
    List<ShoppingCartEntity> findAllWithItems();
}
