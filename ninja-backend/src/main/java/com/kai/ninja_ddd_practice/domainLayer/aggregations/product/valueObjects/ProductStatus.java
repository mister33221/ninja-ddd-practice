package com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects;

public enum ProductStatus {
    PULL_ON_SHELVES("上架"),
    PULL_OFF_SHELVES("下架"),
    OUT_OF_STOCK("缺貨"),
    DISCONTINUED("停產");

    private final String statusDescription;

    ProductStatus(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getStatusDescription() {
        return statusDescription;
    }
}
