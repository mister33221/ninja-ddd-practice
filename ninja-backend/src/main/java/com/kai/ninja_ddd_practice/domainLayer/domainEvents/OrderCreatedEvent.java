package com.kai.ninja_ddd_practice.domainLayer.domainEvents;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 訂單創建事件
 */
@Value
@Builder
public class OrderCreatedEvent implements DomainEvent {
    String eventId;
    String orderId;
    String customerId;
    String status;
    LocalDateTime occurredOn;

    public static OrderCreatedEvent create(String orderId, String customerId, String status) {
        return OrderCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .orderId(orderId)
                .customerId(customerId)
                .status(status)
                .occurredOn(LocalDateTime.now())
                .build();
    }

    @Override
    public String getEventType() {
        return "OrderCreated";
    }

    @Override
    public String getAggregateId() {
        return orderId;
    }
}