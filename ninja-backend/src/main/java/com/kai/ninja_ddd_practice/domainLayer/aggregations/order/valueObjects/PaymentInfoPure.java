package com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects;

import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.Money;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

/**
 * 純淨的支付信息值對象 - 領域層
 * 不包含任何基礎設施依賴
 */
@Value
@Builder
public class PaymentInfoPure {
    String paymentMethod;
    String transactionId;
    Money amount;
    LocalDateTime paymentTime;
    String status;
    String description;

    /**
     * 檢查支付是否成功
     */
    public boolean isSuccessful() {
        return "SUCCESS".equals(status);
    }

    /**
     * 檢查支付是否失敗
     */
    public boolean isFailed() {
        return "FAILED".equals(status);
    }

    /**
     * 檢查支付是否待處理
     */
    public boolean isPending() {
        return "PENDING".equals(status);
    }

    /**
     * 創建成功的支付信息
     */
    public static PaymentInfoPure createSuccessful(String paymentMethod, String transactionId, 
                                                  Money amount, String description) {
        return PaymentInfoPure.builder()
                .paymentMethod(paymentMethod)
                .transactionId(transactionId)
                .amount(amount)
                .paymentTime(LocalDateTime.now())
                .status("SUCCESS")
                .description(description)
                .build();
    }

    /**
     * 創建失敗的支付信息
     */
    public static PaymentInfoPure createFailed(String paymentMethod, Money amount, 
                                              String description) {
        return PaymentInfoPure.builder()
                .paymentMethod(paymentMethod)
                .amount(amount)
                .paymentTime(LocalDateTime.now())
                .status("FAILED")
                .description(description)
                .build();
    }
}
