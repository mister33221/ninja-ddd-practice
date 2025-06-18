package com.kai.ninja_ddd_practice.infrastructureLayer.messaging.kafka;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

/**
 * Kafka 消息格式
 */
@Value
@Builder
public class KafkaMessage {
    String messageId;
    String eventType;
    String aggregateId;
    String payload;
    LocalDateTime timestamp;
    String source;
    String version;
}