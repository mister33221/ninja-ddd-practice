package com.kai.ninja_ddd_practice.domainLayer.domainEvents;

import java.time.LocalDateTime;

/**
 * 領域事件基礎介面
 */
public interface DomainEvent {
    String getEventId();
    String getEventType();
    LocalDateTime getOccurredOn();
    String getAggregateId();
}