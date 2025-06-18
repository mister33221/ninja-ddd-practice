package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.payment;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects.PaymentInfoPure;
import com.kai.ninja_ddd_practice.domainLayer.domainServices.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 支付服務適配器 - 防腐層實作
 * 負責隔離領域層與外部支付服務的技術細節
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceAdapter implements PaymentService {

    private final ExternalPaymentClient externalPaymentClient;

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        try {
            log.info("Processing payment for order: {}", request.getOrderId());
            
            // 1. 轉換領域對象為外部 API 格式
            ExternalPaymentRequest externalRequest = convertToExternalRequest(request);
            
            // 2. 調用外部支付服務
            ExternalPaymentResponse externalResponse = externalPaymentClient.processPayment(externalRequest);
            
            // 3. 轉換外部響應為領域對象
            PaymentResult result = convertToDomainResult(externalResponse);
            
            log.info("Payment processed. Transaction ID: {}, Status: {}", 
                    result.getTransactionId(), result.getStatus());
            
            return result;
            
        } catch (Exception e) {
            log.error("Payment processing failed for order: {}", request.getOrderId(), e);
            return PaymentResult.builder()
                    .transactionId(null)
                    .status(PaymentStatus.FAILED)
                    .message("Payment processing failed: " + e.getMessage())
                    .processedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public PaymentResult refundPayment(String transactionId, PaymentInfoPure refundInfo) {
        try {
            log.info("Processing refund for transaction: {}", transactionId);
            
            ExternalPaymentResponse externalResponse = externalPaymentClient.refund(
                    transactionId, 
                    refundInfo.getAmount().getAmount()
            );
            
            PaymentResult result = convertToDomainResult(externalResponse);
            
            log.info("Refund processed. Transaction ID: {}, Status: {}", 
                    result.getTransactionId(), result.getStatus());
            
            return result;
            
        } catch (Exception e) {
            log.error("Refund processing failed for transaction: {}", transactionId, e);
            return PaymentResult.builder()
                    .transactionId(null)
                    .status(PaymentStatus.FAILED)
                    .message("Refund processing failed: " + e.getMessage())
                    .processedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public PaymentStatus checkPaymentStatus(String transactionId) {
        try {
            log.debug("Checking payment status for transaction: {}", transactionId);
            
            ExternalPaymentResponse response = externalPaymentClient.checkStatus(transactionId);
            return convertToPaymentStatus(response.getStatus());
            
        } catch (Exception e) {
            log.error("Failed to check payment status for transaction: {}", transactionId, e);
            return PaymentStatus.FAILED;
        }
    }

    /**
     * 轉換領域請求為外部 API 請求
     */
    private ExternalPaymentRequest convertToExternalRequest(PaymentRequest request) {
        return ExternalPaymentRequest.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount().getAmount())
                .currency(request.getAmount().getCurrency())
                .customerId(request.getCustomerId())
                .paymentMethod(request.getPaymentMethod())
                .description(request.getDescription())
                .callbackUrl("https://api.ninja-store.com/payment/callback")
                .build();
    }

    /**
     * 轉換外部響應為領域結果
     */
    private PaymentResult convertToDomainResult(ExternalPaymentResponse response) {
        return PaymentResult.builder()
                .transactionId("ninja_" + System.currentTimeMillis()) // 內部交易ID
                .status(convertToPaymentStatus(response.getStatus()))
                .message(response.getMessage())
                .processedAt(parseTimestamp(response.getTimestamp()))
                .externalTransactionId(response.getTransactionId()) // 外部交易ID
                .build();
    }

    /**
     * 轉換外部狀態為領域狀態
     */
    private PaymentStatus convertToPaymentStatus(String externalStatus) {
        return switch (externalStatus.toUpperCase()) {
            case "SUCCESS" -> PaymentStatus.SUCCESS;
            case "FAILED", "ERROR" -> PaymentStatus.FAILED;
            case "PENDING" -> PaymentStatus.PENDING;
            case "CANCELLED" -> PaymentStatus.CANCELLED;
            case "REFUNDED" -> PaymentStatus.REFUNDED;
            default -> PaymentStatus.FAILED;
        };
    }

    /**
     * 解析時間戳
     */
    private LocalDateTime parseTimestamp(String timestamp) {
        try {
            return LocalDateTime.parse(timestamp);
        } catch (Exception e) {
            log.warn("Failed to parse timestamp: {}, using current time", timestamp);
            return LocalDateTime.now();
        }
    }
}
