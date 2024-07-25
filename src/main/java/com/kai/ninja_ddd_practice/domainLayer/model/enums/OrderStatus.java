package com.kai.ninja_ddd_practice.domainLayer.model.enums;

public enum OrderStatus {
    PENDING("待處理"),
    PAID("已支付"),
    SHIPPED("已發貨"),
    DELIVERED("已送達"),
    CANCELLED("已取消");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canCancel() {
        return this == PENDING || this == PAID;
    }
}
