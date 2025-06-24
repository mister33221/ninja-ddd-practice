package com.kai.ninja_ddd_practice.domainLayer.aggregations.user.aggregateRoot;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserId;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserProfilePure;
import com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects.UserCredentialsPure;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 純淨的用戶聚合根 - 不包含任何基礎設施依賴
 */
@Value
@Builder(toBuilder = true)
public class UserPure {
    /**
     * 強型別 ID
     * 一般的 ID 會使用原始型別
     * 但是你看以下兩種 ID
     * 1. Long productId = 123L;
     * 2. Long userId = 456L;
     * 這樣的 ID 會讓人混淆，因為它們都是 Long 型別。
     * 當我這樣用
     * ```java
     * getProductByProductId(userId)
     * ```
     * 他也不會報錯
     * 所以我們會使用強型別 ID 來避免這種混淆。
     */
    UserId id;
    String username;
    UserProfilePure profile;
    UserCredentialsPure credentials;

    /**
     * 更新用戶資訊
     */
    public UserPure updateUserInfo(String username, String fullName, String phoneNumber, 
                                  String address, LocalDate dateOfBirth) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        UserProfilePure updatedProfile = profile.toBuilder()
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .build();

        return this.toBuilder()
                .username(username.trim())
                .profile(updatedProfile)
                .build();
    }

    /**
     * 更新最後登入時間
     */
    public UserPure updateLastLoginTime(LocalDateTime loginTime) {
        UserCredentialsPure updatedCredentials = credentials.toBuilder()
                .lastLoginTime(loginTime)
                .build();

        return this.toBuilder()
                .credentials(updatedCredentials)
                .build();
    }

    /**
     * 驗證密碼
     */
    public boolean verifyPassword(String rawPassword, String salt) {
        return credentials.verifyPassword(rawPassword, salt);
    }

    /**
     * 更新密碼
     */
    public UserPure updatePassword(String newHashedPassword, String newSalt) {
        UserCredentialsPure updatedCredentials = credentials.toBuilder()
                .hashedPassword(newHashedPassword)
                .randomSalt(newSalt)
                .build();

        return this.toBuilder()
                .credentials(updatedCredentials)
                .build();
    }

    /**
     * 檢查用戶是否激活（基於電子郵件存在與否）
     */
    public boolean isActive() {
        return profile != null && 
               profile.getEmail() != null && 
               !profile.getEmail().trim().isEmpty();
    }

    /**
     * 獲取顯示名稱
     */
    public String getDisplayName() {
        if (profile != null && profile.getFullName() != null && !profile.getFullName().trim().isEmpty()) {
            return profile.getFullName();
        }
        return username;
    }

    /**
     * 檢查是否最近登入過（7天內）
     */
    public boolean hasRecentLogin() {
        if (credentials == null || credentials.getLastLoginTime() == null) {
            return false;
        }
        return credentials.getLastLoginTime().isAfter(LocalDateTime.now().minusDays(7));
    }

    /**
     * 創建新用戶的工廠方法
     */
    public static UserPure createNew(String username, String email, String hashedPassword, String salt) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        if (salt == null || salt.trim().isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty");
        }

        UserProfilePure profile = UserProfilePure.builder()
                .email(email.trim().toLowerCase())
                .build();

        UserCredentialsPure credentials = UserCredentialsPure.builder()
                .hashedPassword(hashedPassword)
                .randomSalt(salt)
                .build();

        return UserPure.builder()
                .username(username.trim())
                .profile(profile)
                .credentials(credentials)
                .build();
    }
}
