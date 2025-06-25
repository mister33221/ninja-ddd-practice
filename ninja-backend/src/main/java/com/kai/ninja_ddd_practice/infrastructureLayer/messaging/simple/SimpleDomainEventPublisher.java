package com.kai.ninja_ddd_practice.infrastructureLayer.messaging.simple;

import com.kai.ninja_ddd_practice.domainLayer.domainEvents.DomainEvent;
import com.kai.ninja_ddd_practice.domainLayer.domainServices.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 簡單的領域事件發布者實作
 * 用於替代 Kafka 實作，僅記錄日誌
 */
@Component
@Slf4j
public class SimpleDomainEventPublisher implements DomainEventPublisher {

    @Override
    public void publish(DomainEvent event) {
        // 簡單的日誌記錄實作
        log.info("發布領域事件: {} - ID: {} - 時間: {}", 
                event.getClass().getSimpleName(), 
                event.getEventId(), 
                event.getOccurredOn());
        
        // 在實際生產環境中，這裡可以：
        // 1. 發送到消息隊列 (RabbitMQ, Apache Kafka, etc.)
        // 2. 存儲到事件存儲 (Event Store)
        // 3. 發送到外部系統 (Webhook, HTTP API)
        // 4. 觸發其他應用服務
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
