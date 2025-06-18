package com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.aggregateRoot.ShoppingCartPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.shoppingCart.valueObjects.ShoppingCartId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;

import java.util.List;
import java.util.Optional;

/**
 * 購物車純領域 Repository 介面
 * 不依賴任何基礎設施技術
 */
public interface ShoppingCartPureRepository {

    /**
     * 根據 ID 查找購物車
     */
    Optional<ShoppingCartPure> findById(ShoppingCartId id);

    /**
     * 根據用戶 ID 查找購物車
     */
    Optional<ShoppingCartPure> findByUserId(UserId userId);

    /**
     * 保存購物車
     */
    ShoppingCartPure save(ShoppingCartPure shoppingCart);

    /**
     * 刪除購物車
     */
    void deleteById(ShoppingCartId id);

    /**
     * 根據用戶 ID 刪除購物車
     */
    void deleteByUserId(UserId userId);

    /**
     * 檢查購物車是否存在
     */
    boolean existsById(ShoppingCartId id);

    /**
     * 檢查用戶是否有購物車
     */
    boolean existsByUserId(UserId userId);

    /**
     * 查找所有購物車（通常用於管理功能）
     */
    List<ShoppingCartPure> findAll();

    /**
     * 根據用戶 ID 列表查找購物車
     */
    List<ShoppingCartPure> findByUserIds(List<UserId> userIds);
}
