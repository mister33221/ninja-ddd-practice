package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.payment;

import org.springframework.stereotype.Component;

/**
 * 外部支付 API 客戶端 - 模擬第三方支付服務
 */
@Component
public class ExternalPaymentClient {
    
    public ExternalPaymentResponse processPayment(ExternalPaymentRequest request) {
        // 模擬外部支付 API 調用
        try {
            Thread.sleep(100); // 模擬網路延遲
            
            // 簡單的成功/失敗邏輯（實際應該調用真實的支付 API）
            boolean success = request.getAmount().compareTo(java.math.BigDecimal.valueOf(10000)) <= 0;
            
            return ExternalPaymentResponse.builder()
                    .transactionId("ext_" + System.currentTimeMillis())
                    .status(success ? "SUCCESS" : "FAILED")
                    .message(success ? "Payment processed successfully" : "Payment amount too high")
                    .timestamp(java.time.LocalDateTime.now().toString())
                    .build();
                    
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ExternalPaymentResponse.builder()
                    .transactionId(null)
                    .status("ERROR")
                    .message("Payment processing interrupted")
                    .timestamp(java.time.LocalDateTime.now().toString())
                    .build();
        }
    }
    
    public ExternalPaymentResponse checkStatus(String transactionId) {
        // 模擬狀態查詢
        return ExternalPaymentResponse.builder()
                .transactionId(transactionId)
                .status("SUCCESS")
                .message("Payment completed")
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();
    }
    
    public ExternalPaymentResponse refund(String originalTransactionId, java.math.BigDecimal amount) {
        // 模擬退款
        return ExternalPaymentResponse.builder()
                .transactionId("refund_" + System.currentTimeMillis())
                .status("SUCCESS")
                .message("Refund processed successfully")
                .timestamp(java.time.LocalDateTime.now().toString())
                .build();
    }
}