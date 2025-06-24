package com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects;

import lombok.Value;

/**
 * 用戶ID值對象
 */
@Value // 常用於建立「不可變物件（immutable object）」，特別適合在 DDD 中定義 值對象（Value Object）。它的功能可比喻為 Java 中的「record 強化版」，自動幫你產生一堆樣板程式碼。
public class UserId {
    Long value;

    private UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        this.value = value;
    }

    /**
     * 靜態工廠方法，用於創建 UserId 實例，常用於 DDD 中的值對象模式。
     * @param value 用戶ID值
     * @return UserId 實
     * @param value
     * @return
     */
    public static UserId of(Long value) {
        return new UserId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
