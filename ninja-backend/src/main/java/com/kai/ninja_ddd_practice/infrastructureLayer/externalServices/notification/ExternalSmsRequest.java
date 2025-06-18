package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.notification;

import lombok.Builder;
import lombok.Value;
import java.util.Map;

@Value
@Builder
public class ExternalSmsRequest {
    String phoneNumber;
    String message;
    String templateId;
    Map<String, Object> templateData;
}