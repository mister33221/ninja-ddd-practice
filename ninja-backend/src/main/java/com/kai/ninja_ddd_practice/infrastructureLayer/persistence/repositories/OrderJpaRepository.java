package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories;

import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 訂單 JPA Repository
 */
@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * 根據用戶 ID 查找訂單
     */
    List<OrderEntity> findByUserId(Long userId);

    /**
     * 根據狀態查找訂單
     */
    List<OrderEntity> findByStatus(OrderEntity.OrderStatusEnum status);

    /**
     * 根據用戶 ID 和狀態查找訂單
     */
    List<OrderEntity> findByUserIdAndStatus(Long userId, OrderEntity.OrderStatusEnum status);

    /**
     * 根據時間範圍查找訂單
     */
    List<OrderEntity> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根據用戶 ID 和時間範圍查找訂單
     */
    List<OrderEntity> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查找待處理的訂單（超過指定時間）
     */
    List<OrderEntity> findByStatusAndCreatedAtBefore(OrderEntity.OrderStatusEnum status, LocalDateTime cutoffTime);

    /**
     * 統計用戶訂單數量
     */
    long countByUserId(Long userId);

    /**
     * 統計特定狀態的訂單數量
     */
    long countByStatus(OrderEntity.OrderStatusEnum status);

    /**
     * 查找訂單（包含項目）
     */
    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithItems(@Param("id") Long id);

    /**
     * 查找用戶的訂單（包含項目）
     */
    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items WHERE o.userId = :userId")
    List<OrderEntity> findByUserIdWithItems(@Param("userId") Long userId);

    /**
     * 查找所有訂單（包含項目）
     */
    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items")
    List<OrderEntity> findAllWithItems();

    /**
     * 根據狀態查找訂單（包含項目）
     */
    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.items WHERE o.status = :status")
    List<OrderEntity> findByStatusWithItems(@Param("status") OrderEntity.OrderStatusEnum status);
}
