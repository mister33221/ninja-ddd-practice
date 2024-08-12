package com.kai.ninja_ddd_practice.domainLayer.aggregations.order.valueObjects;

public enum OrderStatus {
    PENDING("PENDING", "待處理"),
    PAID("PAID", "已支付"),
    SHIPPED("SHIPPED", "已發貨"),
    DELIVERED("DELIVERED", "已送達"),
    CANCELLED("CANCELLED", "已取消");

    private final String status;
    private final String statusDescription;

    OrderStatus(String status, String statusDescription) {
        this.status = status;
        this.statusDescription = statusDescription;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public boolean canCancel() {
        return this == PENDING || this == PAID;
    }
}
