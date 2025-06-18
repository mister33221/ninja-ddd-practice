package com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects;

import lombok.Value;
import java.math.BigDecimal;

/**
 * 金錢值對象
 */
@Value
public class Money {
    BigDecimal amount;
    String currency;
    
    public Money(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        this.amount = amount;
        this.currency = currency;
    }
    
    public Money(BigDecimal amount) {
        this(amount, "TWD"); // 預設為台幣
    }
    
    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }
    
    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }
    
    public boolean isNegativeOrZero() {
        return amount.compareTo(BigDecimal.ZERO) <= 0;
    }
    
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currency);
    }
}