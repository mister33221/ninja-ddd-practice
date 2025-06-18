package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.payment;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;

/**
 * 外部支付 API 請求格式
 */
@Value
@Builder
public class ExternalPaymentRequest {
    String orderId;
    BigDecimal amount;
    String currency;
    String customerId;
    String paymentMethod;
    String description;
    String callbackUrl;
}