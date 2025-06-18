package com.kai.ninja_ddd_practice.domainLayer.aggregations.user.valueObjects;

import lombok.Value;

/**
 * 用戶ID值對象
 */
@Value
public class UserId {
    Long value;

    private UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        this.value = value;
    }

    public static UserId of(Long value) {
        return new UserId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}