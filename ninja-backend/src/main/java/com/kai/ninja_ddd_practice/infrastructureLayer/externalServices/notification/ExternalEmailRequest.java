package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.notification;

import lombok.Builder;
import lombok.Value;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class ExternalEmailRequest {
    String to;
    List<String> cc;
    List<String> bcc;
    String subject;
    String content;
    boolean isHtml;
    String templateId;
    Map<String, Object> templateData;
}