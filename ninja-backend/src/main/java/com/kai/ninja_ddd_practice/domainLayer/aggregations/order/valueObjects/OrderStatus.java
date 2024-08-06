package com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects;

public enum OrderStatus {
    PENDING("待處理"),
    PAID("已支付"),
    SHIPPED("已發貨"),
    DELIVERED("已送達"),
    CANCELLED("已取消");

    private final String statusDescription;

    OrderStatus(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public boolean canCancel() {
        return this == PENDING || this == PAID;
    }
}
