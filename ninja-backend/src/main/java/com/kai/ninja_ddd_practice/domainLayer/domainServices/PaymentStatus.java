package com.kai.ninja_ddd_practice.domainLayer.domainServices;

/**
 * 支付狀態枚舉
 */
public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    CANCELLED,
    REFUNDED
}