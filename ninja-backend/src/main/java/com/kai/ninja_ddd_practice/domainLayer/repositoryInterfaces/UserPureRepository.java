package com.kai.ninja_ddd_practice.domainLayer.repositoryInterfaces;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot.UserPure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;

import java.util.List;
import java.util.Optional;

/**
 * 純淨的用戶 Repository 介面 - 不包含任何基礎設施依賴
 */
public interface UserPureRepository {
    
    /**
     * 根據ID查找用戶
     */
    Optional<UserPure> findById(UserId id);
    
    /**
     * 根據用戶名查找用戶
     */
    Optional<UserPure> findByUsername(String username);
    
    /**
     * 根據電子郵件查找用戶
     */
    Optional<UserPure> findByEmail(String email);
    
    /**
     * 檢查用戶名是否已存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 檢查電子郵件是否已存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 保存用戶
     */
    UserPure save(UserPure user);
    
    /**
     * 刪除用戶
     */
    void deleteById(UserId id);
    
    /**
     * 查找所有活躍用戶
     */
    List<UserPure> findAllActive();
    
    /**
     * 查找最近登入的用戶
     */
    List<UserPure> findRecentlyLoggedIn(int days);
}