package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 外部簡訊服務客戶端
 */
@Component
@Slf4j
public class SmsServiceClient {
    
    public ExternalSmsResponse sendSms(ExternalSmsRequest request) {
        try {
            Thread.sleep(30);
            
            log.info("Simulating SMS send to: {} with message: {}", 
                    request.getPhoneNumber(), request.getMessage());
            
            return ExternalSmsResponse.builder()
                    .success(true)
                    .messageId("sms_" + System.currentTimeMillis())
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
                    
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ExternalSmsResponse.builder()
                    .success(false)
                    .errorMessage("SMS service interrupted")
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
        }
    }
}