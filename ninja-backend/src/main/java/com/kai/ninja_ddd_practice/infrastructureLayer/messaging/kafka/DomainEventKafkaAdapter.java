package com.kai.ninja_ddd_practice.infrastructureLayer.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.ninja_ddd_practice.domainLayer.domainEvents.DomainEvent;
import com.kai.ninja_ddd_practice.domainLayer.domainServices.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Kafka 領域事件適配器 - 防腐層實作
 * 負責隔離領域層與 Kafka 技術細節
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DomainEventKafkaAdapter implements DomainEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(DomainEvent event) {
        try {
            log.info("Publishing domain event: {} for aggregate: {}", 
                    event.getEventType(), event.getAggregateId());

            // 1. 轉換領域事件為 Kafka 消息格式
            KafkaMessage kafkaMessage = convertToKafkaMessage(event);
            
            // 2. 序列化為 JSON
            String messageJson = objectMapper.writeValueAsString(kafkaMessage);
            
            // 3. 發送到對應的 Topic
            String topic = getTopicName(event.getEventType());
            kafkaTemplate.send(topic, event.getAggregateId(), messageJson)
                    .whenComplete((result, failure) -> {
                        if (failure != null) {
                            log.error("Failed to publish event to topic: {}", topic, failure);
                        } else {
                            log.info("Event published successfully to topic: {}, partition: {}, offset: {}", 
                                    topic, 
                                    result.getRecordMetadata().partition(), 
                                    result.getRecordMetadata().offset());
                        }
                    });
            
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize domain event: {}", event.getEventType(), e);
            throw new RuntimeException("Event serialization failed", e);
        } catch (Exception e) {
            log.error("Failed to publish domain event: {}", event.getEventType(), e);
            throw new RuntimeException("Event publishing failed", e);
        }
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        log.info("Publishing {} domain events", events.size());
        
        for (DomainEvent event : events) {
            try {
                publish(event);
            } catch (Exception e) {
                log.error("Failed to publish event in batch: {}", event.getEventType(), e);
                // 繼續處理其他事件，不因單個事件失敗而停止
            }
        }
    }

    /**
     * 轉換領域事件為 Kafka 消息格式
     */
    private KafkaMessage convertToKafkaMessage(DomainEvent event) {
        return KafkaMessage.builder()
                .messageId(UUID.randomUUID().toString())
                .eventType(event.getEventType())
                .aggregateId(event.getAggregateId())
                .payload(serializeEventPayload(event))
                .timestamp(LocalDateTime.now())
                .source("ninja-backend")
                .version("1.0")
                .build();
    }

    /**
     * 序列化事件負載
     */
    private String serializeEventPayload(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event payload for event: {}", event.getEventType(), e);
            return "{}"; // 返回空 JSON 對象作為降級方案
        }
    }

    /**
     * 根據事件類型獲取對應的 Topic 名稱
     */
    private String getTopicName(String eventType) {
        return switch (eventType) {
            case "OrderCreated" -> "order-created";
            case "OrderUpdated" -> "order-updated";
            case "OrderCancelled" -> "order-cancelled";
            case "ProductStockChanged" -> "product-stock-changed";
            case "UserRegistered" -> "user-registered";
            case "PaymentProcessed" -> "payment-processed";
            default -> "domain-events"; // 預設 Topic
        };
    }
}