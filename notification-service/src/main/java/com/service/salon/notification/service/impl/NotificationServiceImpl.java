package com.service.salon.notification.service.impl;

import com.service.salon.notification.model.ClientFcmToken;
import com.service.salon.notification.model.Notification;
import com.service.salon.notification.model.dto.NotificationRequestDto;
import com.service.salon.notification.repository.ClientFcmTokenRepository;
import com.service.salon.notification.repository.NotificationRepository;
import com.service.salon.notification.service.FcmService;
import com.service.salon.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ClientFcmTokenRepository fcmTokenRepository;
    private final FcmService fcmService;

    @Override
    @Transactional
    public Notification createNotification(String clientPhone, NotificationRequestDto dto) {
        Notification notification = Notification.builder()
                .clientPhone(clientPhone)
                .title(dto.getTitle())
                .body(dto.getBody())
                .type(dto.getType())
                .isRead(false)
                .build();
        Notification saved = notificationRepository.save(notification);

        // Отправка push через FCM
        fcmTokenRepository.findByPhoneNumber(clientPhone).ifPresent(token -> {
            fcmService.sendPushNotification(token.getFcmToken(), dto.getTitle(), dto.getBody());
        });

        return saved;
    }

    @Override
    public List<Notification> getClientNotifications(String clientPhone) {
        return notificationRepository.findByClientPhoneOrderByCreatedAtDesc(clientPhone);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    public int getUnreadCount(String clientPhone) {
        return notificationRepository.countByClientPhoneAndIsReadFalse(clientPhone);
    }

    @Override
    @Transactional
    public void saveFcmToken(String clientPhone, String fcmToken) {
        ClientFcmToken tokenEntity = ClientFcmToken.builder()
                .phoneNumber(clientPhone)
                .fcmToken(fcmToken)
                .build();
        // upsert: save перезапишет по первичному ключу
        fcmTokenRepository.save(tokenEntity);
    }
}
