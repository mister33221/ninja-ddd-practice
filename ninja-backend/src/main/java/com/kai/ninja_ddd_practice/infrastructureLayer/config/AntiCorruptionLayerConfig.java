package com.kai.ninja_ddd_practice.infrastructureLayer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 防腐層配置 - 統一配置防腐層相關的 Bean
 */
@Configuration
public class AntiCorruptionLayerConfig {

    /**
     * 配置用於防腐層的 ObjectMapper
     * 統一處理序列化和反序列化
     */
    @Bean
    @Primary
    public ObjectMapper antiCorruptionObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // 註冊 Java Time 模組
        mapper.registerModule(new JavaTimeModule());
        
        // 配置序列化設定
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        
        // 配置反序列化設定
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        
        return mapper;
    }
}