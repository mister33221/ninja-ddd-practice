package com.kai.ninja_ddd_practice.domainLayer.domainServices;

import com.kai.ninja_ddd_practice.domainLayer.domainEvents.DomainEvent;

/**
 * 領域事件發布者介面 - 純粹的領域服務定義
 */
public interface DomainEventPublisher {
    
    void publish(DomainEvent event);
    
    void publishAll(java.util.List<DomainEvent> events);
}