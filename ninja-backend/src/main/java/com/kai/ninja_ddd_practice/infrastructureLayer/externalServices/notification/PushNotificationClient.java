package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 推播通知服務客戶端
 */
@Component
@Slf4j
public class PushNotificationClient {
    
    public ExternalPushResponse sendPush(ExternalPushRequest request) {
        try {
            Thread.sleep(20);
            
            log.info("Simulating push notification to user: {} with title: {}", 
                    request.getUserId(), request.getTitle());
            
            return ExternalPushResponse.builder()
                    .success(true)
                    .messageId("push_" + System.currentTimeMillis())
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
                    
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ExternalPushResponse.builder()
                    .success(false)
                    .errorMessage("Push service interrupted")
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
        }
    }
}