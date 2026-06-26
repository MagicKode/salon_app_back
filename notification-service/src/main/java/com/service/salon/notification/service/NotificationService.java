package com.service.salon.notification.service;

import com.service.salon.notification.model.Notification;
import com.service.salon.notification.model.dto.NotificationRequestDto;
import jakarta.validation.Valid;

import java.util.List;

public interface NotificationService {
    Notification createNotification(String clientPhone, NotificationRequestDto dto);
    List<Notification> getClientNotifications(String clientPhone);
    void markAsRead(Long notificationId);
    int getUnreadCount(String clientPhone);

    void saveFcmToken(String clientPhone, String fcmToken);

    int broadcastToAllClients(String masterPhone, NotificationRequestDto dto);
}
