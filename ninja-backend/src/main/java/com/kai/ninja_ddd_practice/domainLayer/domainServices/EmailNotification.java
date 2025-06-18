package com.kai.ninja_ddd_practice.domainLayer.domainServices;

import lombok.Builder;
import lombok.Value;
import java.util.List;

/**
 * 電子郵件通知領域對象
 */
@Value
@Builder
public class EmailNotification {
    String to;
    List<String> cc;
    List<String> bcc;
    String subject;
    String content;
    String templateId;
    java.util.Map<String, Object> templateData;
    boolean isHtml;
}