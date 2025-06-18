package com.kai.ninja_ddd_practice.infrastructureLayer.externalServices.notification;

import com.kai.ninja_ddd_practice.domainLayer.domainServices.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 通知服務適配器 - 防腐層實作
 * 整合多種通知渠道（郵件、簡訊、推播）
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceAdapter implements NotificationService {

    private final EmailServiceClient emailServiceClient;
    private final SmsServiceClient smsServiceClient;
    private final PushNotificationClient pushNotificationClient;

    @Override
    public void sendEmail(EmailNotification notification) {
        try {
            log.info("Sending email to: {}", notification.getTo());
            
            // 轉換為外部服務格式並發送
            ExternalEmailRequest request = convertToEmailRequest(notification);
            ExternalEmailResponse response = emailServiceClient.sendEmail(request);
            
            if (response.isSuccess()) {
                log.info("Email sent successfully. Message ID: {}", response.getMessageId());
            } else {
                log.error("Failed to send email: {}", response.getErrorMessage());
            }
            
        } catch (Exception e) {
            log.error("Email sending failed for recipient: {}", notification.getTo(), e);
        }
    }

    @Override
    public void sendSms(SmsNotification notification) {
        try {
            log.info("Sending SMS to: {}", notification.getPhoneNumber());
            
            // 轉換為外部服務格式並發送
            ExternalSmsRequest request = convertToSmsRequest(notification);
            ExternalSmsResponse response = smsServiceClient.sendSms(request);
            
            if (response.isSuccess()) {
                log.info("SMS sent successfully. Message ID: {}", response.getMessageId());
            } else {
                log.error("Failed to send SMS: {}", response.getErrorMessage());
            }
            
        } catch (Exception e) {
            log.error("SMS sending failed for phone: {}", notification.getPhoneNumber(), e);
        }
    }

    @Override
    public void sendPushNotification(PushNotification notification) {
        try {
            log.info("Sending push notification to user: {}", notification.getUserId());
            
            // 轉換為外部服務格式並發送
            ExternalPushRequest request = convertToPushRequest(notification);
            ExternalPushResponse response = pushNotificationClient.sendPush(request);
            
            if (response.isSuccess()) {
                log.info("Push notification sent successfully. Message ID: {}", response.getMessageId());
            } else {
                log.error("Failed to send push notification: {}", response.getErrorMessage());
            }
            
        } catch (Exception e) {
            log.error("Push notification sending failed for user: {}", notification.getUserId(), e);
        }
    }

    // 轉換方法
    private ExternalEmailRequest convertToEmailRequest(EmailNotification notification) {
        return ExternalEmailRequest.builder()
                .to(notification.getTo())
                .cc(notification.getCc())
                .bcc(notification.getBcc())
                .subject(notification.getSubject())
                .content(notification.getContent())
                .isHtml(notification.isHtml())
                .templateId(notification.getTemplateId())
                .templateData(notification.getTemplateData())
                .build();
    }

    private ExternalSmsRequest convertToSmsRequest(SmsNotification notification) {
        return ExternalSmsRequest.builder()
                .phoneNumber(notification.getPhoneNumber())
                .message(notification.getMessage())
                .templateId(notification.getTemplateId())
                .templateData(notification.getTemplateData())
                .build();
    }

    private ExternalPushRequest convertToPushRequest(PushNotification notification) {
        return ExternalPushRequest.builder()
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .data(notification.getData())
                .priority(notification.getPriority())
                .build();
    }
}