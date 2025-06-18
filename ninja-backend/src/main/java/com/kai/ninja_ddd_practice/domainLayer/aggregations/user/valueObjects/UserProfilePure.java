package com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * 純淨的用戶資料值對象 - 不包含任何基礎設施依賴
 */
@Value
@Builder(toBuilder = true)
public class UserProfilePure {
    String fullName;
    String email;
    String phoneNumber;
    LocalDate dateOfBirth;
    String address;

    /**
     * 驗證電子郵件格式
     */
    public boolean hasValidEmail() {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }

    /**
     * 檢查個人資料是否完整
     */
    public boolean isProfileComplete() {
        return fullName != null && !fullName.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               hasValidEmail();
    }

    /**
     * 獲取年齡
     */
    public Integer getAge() {
        if (dateOfBirth == null) {
            return null;
        }
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    /**
     * 檢查是否成年
     */
    public boolean isAdult() {
        Integer age = getAge();
        return age != null && age >= 18;
    }

    /**
     * 更新基本資訊的便利方法
     */
    public UserProfilePure updateBasicInfo(String fullName, String phoneNumber, String address) {
        return this.toBuilder()
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();
    }
}