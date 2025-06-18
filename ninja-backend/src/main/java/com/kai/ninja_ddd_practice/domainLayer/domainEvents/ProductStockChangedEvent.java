package com.kai.ninja_ddd_practice.domainLayer.domainEvents;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 產品庫存變更事件
 */
@Value
@Builder
public class ProductStockChangedEvent implements DomainEvent {
    String eventId;
    String productId;
    int oldStock;
    int newStock;
    String reason;
    LocalDateTime occurredOn;

    public static ProductStockChangedEvent create(String productId, int oldStock, int newStock, String reason) {
        return ProductStockChangedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .productId(productId)
                .oldStock(oldStock)
                .newStock(newStock)
                .reason(reason)
                .occurredOn(LocalDateTime.now())
                .build();
    }

    @Override
    public String getEventType() {
        return "ProductStockChanged";
    }

    @Override
    public String getAggregateId() {
        return productId;
    }
}