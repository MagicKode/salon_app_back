package com.service.salon.notification.controller;

import com.service.salon.notification.model.Notification;
import com.service.salon.notification.model.dto.NotificationRequestDto;
import com.service.salon.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(
            @RequestHeader("X-User-Name") String clientPhone) {
        return ResponseEntity.ok(notificationService.getClientNotifications(clientPhone));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadCount(
            @RequestHeader("X-User-Name") String clientPhone) {
        return ResponseEntity.ok(notificationService.getUnreadCount(clientPhone));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(
            @RequestHeader("X-User-Name") String clientPhone,
            @RequestBody NotificationRequestDto dto) {
        return ResponseEntity.ok(notificationService.createNotification(clientPhone, dto));
    }

    @PostMapping("/token")
    public ResponseEntity<Void> updateFcmToken(
            @RequestHeader("X-User-Name") String clientPhone,
            @RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        notificationService.saveFcmToken(clientPhone, token);
        return ResponseEntity.ok().build();
    }
}
