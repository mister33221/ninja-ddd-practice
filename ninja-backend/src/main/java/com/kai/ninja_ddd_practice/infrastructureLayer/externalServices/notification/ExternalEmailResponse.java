package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.notification;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class ExternalEmailResponse {
    boolean success;
    String messageId;
    String errorMessage;
    LocalDateTime timestamp;
}