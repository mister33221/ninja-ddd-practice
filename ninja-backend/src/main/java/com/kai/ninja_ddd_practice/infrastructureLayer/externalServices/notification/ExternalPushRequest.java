package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.notification;

import lombok.Builder;
import lombok.Value;
import java.util.Map;

@Value
@Builder
public class ExternalPushRequest {
    String userId;
    String title;
    String message;
    Map<String, Object> data;
    String priority;
}