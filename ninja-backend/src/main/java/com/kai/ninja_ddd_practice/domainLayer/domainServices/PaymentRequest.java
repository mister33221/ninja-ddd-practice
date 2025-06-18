package com.kai.ninja_ddd_practice.domainLayer.domainServices;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.Money;
import lombok.Builder;
import lombok.Value;

/**
 * 支付請求領域對象
 */
@Value
@Builder
public class PaymentRequest {
    String orderId;
    Money amount;
    String customerId;
    String paymentMethod;
    String description;
}