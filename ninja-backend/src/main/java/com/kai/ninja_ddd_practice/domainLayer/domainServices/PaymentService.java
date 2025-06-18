package com.kai.ninja_ddd_practice.domainLayer.domainServices;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects.PaymentInfoPure;

/**
 * 支付服務領域介面 - 純粹的領域服務定義
 */
public interface PaymentService {
    
    PaymentResult processPayment(PaymentRequest request);
    
    PaymentResult refundPayment(String transactionId, PaymentInfoPure refundInfo);
    
    PaymentStatus checkPaymentStatus(String transactionId);
}
