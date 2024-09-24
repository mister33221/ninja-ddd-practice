package com.kai.ninja_ddd_practice.infrastructureLayer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder.name("order-created")
                .partitions(1)
                .replicas(1)
                .build();
    }


}
