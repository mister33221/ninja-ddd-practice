package com.kai.ninja_ddd_practice.domainLayer.domainServices;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

/**
 * 支付結果領域對象
 */
@Value
@Builder
public class PaymentResult {
    String transactionId;
    PaymentStatus status;
    String message;
    LocalDateTime processedAt;
    String externalTransactionId;
}