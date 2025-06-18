package com.kai.ninja_ddd_practice.domainLayer.domainServices;

/**
 * 通知服務領域介面
 */
public interface NotificationService {
    
    void sendEmail(EmailNotification notification);
    
    void sendSms(SmsNotification notification);
    
    void sendPushNotification(PushNotification notification);
}