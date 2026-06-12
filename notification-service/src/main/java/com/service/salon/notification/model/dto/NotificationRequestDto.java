package com.service.salon.notification.model.dto;

import com.service.salon.notification.model.notificationtypes.NotificationType;
import lombok.Data;

@Data
public class NotificationRequestDto {
    private String title;
    private String body;
    private NotificationType type;
}
