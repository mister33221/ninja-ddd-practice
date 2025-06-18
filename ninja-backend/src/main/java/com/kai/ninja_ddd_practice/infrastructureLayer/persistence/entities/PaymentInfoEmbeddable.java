package com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付信息嵌入式實體 - 用於 Order 實體的資料庫映射
 * 這是基礎設施層的資料存儲對象，不包含業務邏輯
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
}