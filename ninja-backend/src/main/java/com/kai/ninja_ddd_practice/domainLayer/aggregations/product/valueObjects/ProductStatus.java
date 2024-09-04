package com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects;

public enum ProductStatus {
    PULL_ON_SHELVES("PULL_ON_SHELVES", "上架"),
    PULL_OFF_SHELVES("PULL_OFF_SHELVES", "下架"),
    OUT_OF_STOCK("OUT_OF_STOCK", "缺貨"),
    DISCONTINUED("DISCONTINUED", "停產");

    private final String status;
    private final String statusDescription;

    ProductStatus(String status, String statusDescription) {
        this.status = status;
        this.statusDescription = statusDescription;
    }

    public String getStatusDescription() {
        return statusDescription;
    }
    public String getStatus() {
        return status;
    }
}
