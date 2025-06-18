package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.payment;

import lombok.Builder;
import lombok.Value;

/**
 * 外部支付 API 響應格式
 */
@Value
@Builder
public class ExternalPaymentResponse {
    String transactionId;
    String status;
    String message;
    String timestamp;
    String paymentUrl; // 用於跳轉支付頁面
}