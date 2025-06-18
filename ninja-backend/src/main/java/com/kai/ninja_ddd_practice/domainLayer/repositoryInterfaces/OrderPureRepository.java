package com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.aggregateRoot.OrderPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects.OrderId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects.OrderStatus;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 訂單純領域 Repository 介面
 * 不依賴任何基礎設施技術
 */
public interface OrderPureRepository {

    /**
     * 根據 ID 查找訂單
     */
    Optional<OrderPure> findById(OrderId id);

    /**
     * 根據用戶 ID 查找訂單列表
     */
    List<OrderPure> findByUserId(UserId userId);

    /**
     * 根據狀態查找訂單
     */
    List<OrderPure> findByStatus(OrderStatus status);

    /**
     * 根據用戶 ID 和狀態查找訂單
     */
    List<OrderPure> findByUserIdAndStatus(UserId userId, OrderStatus status);

    /**
     * 保存訂單
     */
    OrderPure save(OrderPure order);

    /**
     * 刪除訂單
     */
    void deleteById(OrderId id);

    /**
     * 檢查訂單是否存在
     */
    boolean existsById(OrderId id);

    /**
     * 查找所有訂單
     */
    List<OrderPure> findAll();

    /**
     * 根據時間範圍查找訂單
     */
    List<OrderPure> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根據用戶 ID 和時間範圍查找訂單
     */
    List<OrderPure> findByUserIdAndCreatedAtBetween(UserId userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查找待處理的訂單（超過指定時間）
     */
    List<OrderPure> findPendingOrdersOlderThan(LocalDateTime cutoffTime);

    /**
     * 統計用戶訂單數量
     */
    long countByUserId(UserId userId);

    /**
     * 統計特定狀態的訂單數量
     */
    long countByStatus(OrderStatus status);
}
