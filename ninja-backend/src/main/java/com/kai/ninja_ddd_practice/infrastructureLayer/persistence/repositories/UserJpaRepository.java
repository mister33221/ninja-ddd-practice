package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.repositories;

import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用戶 JPA Repository - 基礎設施層，處理數據庫操作
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    
    /**
     * 根據用戶名查找用戶（包含憑證信息）
     */
    @EntityGraph(attributePaths = {"credentials"})
    Optional<UserEntity> findByUsername(String username);
    
    /**
     * 根據電子郵件查找用戶
     */
    Optional<UserEntity> findByProfileEmail(String email);
    
    /**
     * 檢查用戶名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 檢查電子郵件是否存在
     */
    boolean existsByProfileEmail(String email);
    
    /**
     * 查找有電子郵件的活躍用戶
     */
    @Query("SELECT u FROM UserEntity u WHERE u.profile.email IS NOT NULL AND u.profile.email != ''")
    List<UserEntity> findAllActiveUsers();
    
    /**
     * 查找最近登入的用戶
     */
    @Query("SELECT u FROM UserEntity u WHERE u.credentials.lastLoginTime >= :cutoffTime")
    List<UserEntity> findRecentlyLoggedInUsers(@Param("cutoffTime") LocalDateTime cutoffTime);
}