package com.kai.ninja_ddd_practice.domainLayer.domainServices;

import lombok.Builder;
import lombok.Value;

/**
 * 簡訊通知領域對象
 */
@Value
@Builder
public class SmsNotification {
    String phoneNumber;
    String message;
    String templateId;
    java.util.Map<String, Object> templateData;
}