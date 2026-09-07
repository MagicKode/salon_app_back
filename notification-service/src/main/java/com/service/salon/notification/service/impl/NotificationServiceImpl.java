package com.service.salon.notification.service.impl;

import com.service.salon.commonservice.exception.BusinessException;
import com.service.salon.notification.model.ClientFcmToken;
import com.service.salon.notification.model.Notification;
import com.service.salon.notification.model.dto.NotificationRequestDto;
import com.service.salon.notification.model.notificationtypes.NotificationType;
import com.service.salon.notification.repository.ClientFcmTokenRepository;
import com.service.salon.notification.repository.NotificationRepository;
import com.service.salon.notification.service.FcmService;
import com.service.salon.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ClientFcmTokenRepository fcmTokenRepository;
    private final FcmService fcmService;
    private final JdbcTemplate jdbcTemplate;

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

    @Override
    @Transactional
    public int broadcastToAllClients(String masterPhone, NotificationRequestDto dto) {
        // 1. Проверяем, что отправитель – мастер (защита от клиентов)
        String role = jdbcTemplate.queryForObject("SELECT role FROM users WHERE phone_number = ?", String.class, masterPhone);
        if (!"MASTER".equals(role)) {
            throw new BusinessException("Только мастер может делать рассылку", HttpStatus.FORBIDDEN);
        }

        // 2. Получаем номера всех клиентов
        List<String> clientPhones = jdbcTemplate.queryForList(
                "SELECT phone_number FROM users WHERE role = 'CLIENT'", String.class);

        // 3. Создаём уведомления
        clientPhones.forEach(phone -> {
            Notification notification = Notification.builder()
                    .clientPhone(phone)
                    .title(dto.getTitle())
                    .body(dto.getBody())
                    .type(NotificationType.valueOf(String.valueOf(dto.getType())))
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
            // опционально отправляем push
            fcmTokenRepository.findByPhoneNumber(phone).ifPresent(token ->
                    fcmService.sendPushNotification(token.getFcmToken(), dto.getTitle(), dto.getBody()));
        });

        return clientPhones.size();
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
