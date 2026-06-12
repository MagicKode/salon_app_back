package com.service.salon.notification.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.service.salon.notification.service.FcmService;
import org.springframework.stereotype.Service;

@Service
public class FcmServiceImpl implements FcmService {
    @Override
    public void sendPushNotification(String fcmToken, String title, String body) {
        if (fcmToken == null || fcmToken.isEmpty()) return;

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
//                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent FCM: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("FCM error: " + e.getMessage());
        }
    }
}
