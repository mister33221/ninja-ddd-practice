package com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 純淨的用戶憑證值對象 - 不包含任何基礎設施依賴
 */
@Value
@Builder(toBuilder = true)
public class UserCredentialsPure {
    String hashedPassword;
    LocalDateTime lastLoginTime;
    String randomSalt;

    /**
     * 驗證密碼
     */
    public boolean verifyPassword(String rawPassword, String providedSalt) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            return false;
        }
        if (providedSalt == null || !providedSalt.equals(randomSalt)) {
            return false;
        }
        
        // 這裡應該使用實際的密碼雜湊驗證邏輯
        // 暫時使用簡單的字串比較作為示例
        String expectedHash = hashPassword(rawPassword, randomSalt);
        return expectedHash.equals(hashedPassword);
    }

    /**
     * 檢查密碼是否過期（假設90天過期）
     */
    public boolean isPasswordExpired() {
        if (lastLoginTime == null) {
            return false; // 新用戶，密碼不算過期
        }
        return lastLoginTime.isBefore(LocalDateTime.now().minusDays(90));
    }

    /**
     * 檢查是否需要強制登出（長時間未登入）
     */
    public boolean shouldForceLogout() {
        if (lastLoginTime == null) {
            return false;
        }
        return lastLoginTime.isBefore(LocalDateTime.now().minusDays(30));
    }

    /**
     * 記錄登入時間
     */
    public UserCredentialsPure recordLogin() {
        return this.toBuilder()
                .lastLoginTime(LocalDateTime.now())
                .build();
    }

    /**
     * 簡單的密碼雜湊實作（實際應用中應使用 BCrypt 等安全演算法）
     */
    private String hashPassword(String rawPassword, String salt) {
        // 這是一個簡化的實作，實際應用中應使用安全的雜湊演算法
        return rawPassword + salt; // 僅作為示例
    }

    /**
     * 創建新憑證的工廠方法
     */
    public static UserCredentialsPure createNew(String hashedPassword, String salt) {
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        if (salt == null || salt.trim().isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty");
        }

        return UserCredentialsPure.builder()
                .hashedPassword(hashedPassword)
                .randomSalt(salt)
                .build();
    }
}