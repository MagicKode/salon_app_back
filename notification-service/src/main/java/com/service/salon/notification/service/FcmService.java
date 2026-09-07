package com.service.salon.notification.service;

public interface FcmService {
    void sendPushNotification(String fcmToken, String title, String body);
}
