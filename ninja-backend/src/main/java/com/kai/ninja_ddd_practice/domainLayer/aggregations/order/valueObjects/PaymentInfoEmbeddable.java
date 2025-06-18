package com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付信息嵌入式值對象 - 適合 JPA 映射
 * 這是一個過渡性的解決方案，將 Money 拆解為基本類型
 * TODO: 完整重構後，應該有專門的 PaymentInfoEntity 和純 PaymentInfoPure
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInfoEmbeddable {
    
    @Column(name = "payment_method")
    private String paymentMethod;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "payment_amount", precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "payment_currency", length = 3)
    private String currency;
    
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;
    
    @Column(name = "payment_status", length = 20)
    private String status;
    
    @Column(name = "payment_description")
    private String description;
    
    /**
     * 轉換為純領域對象
     */
    public PaymentInfoPure toDomainObject() {
        return PaymentInfoPure.builder()
                .paymentMethod(paymentMethod)
                .transactionId(transactionId)
                .amount(com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.Money.of(amount, currency != null ? currency : "TWD"))
                .paymentTime(paymentTime)
                .status(status)
                .description(description)
                .build();
    }
    
    /**
     * 從純領域對象創建
     */
    public static PaymentInfoEmbeddable fromDomainObject(PaymentInfoPure domainObject) {
        if (domainObject == null) {
            return null;
        }
        
        return PaymentInfoEmbeddable.builder()
                .paymentMethod(domainObject.getPaymentMethod())
                .transactionId(domainObject.getTransactionId())
                .amount(domainObject.getAmount().getAmount())
                .currency(domainObject.getAmount().getCurrency())
                .paymentTime(domainObject.getPaymentTime())
                .status(domainObject.getStatus())
                .description(domainObject.getDescription())
                .build();
    }
}