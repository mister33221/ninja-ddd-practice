package com.kai.ninja_ddd_practice.domainLayer.domainServices;

import lombok.Builder;
import lombok.Value;
import java.util.Map;

/**
 * 推播通知領域對象
 */
@Value
@Builder
public class PushNotification {
    String userId;
    String title;
    String message;
    Map<String, Object> data;
    String priority;
}