package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 外部郵件服務客戶端 - 模擬第三方郵件服務
 */
@Component
@Slf4j
public class EmailServiceClient {
    
    public ExternalEmailResponse sendEmail(ExternalEmailRequest request) {
        // 模擬外部郵件服務調用
        try {
            Thread.sleep(50); // 模擬網路延遲
            
            log.info("Simulating email send to: {} with subject: {}", 
                    request.getTo(), request.getSubject());
            
            // 模擬成功響應
            return ExternalEmailResponse.builder()
                    .success(true)
                    .messageId("email_" + System.currentTimeMillis())
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
                    
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ExternalEmailResponse.builder()
                    .success(false)
                    .errorMessage("Email service interrupted")
                    .timestamp(java.time.LocalDateTime.now())
                    .build();
        }
    }
}