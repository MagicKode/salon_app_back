package com.service.salon.notification.repository;

import com.service.salon.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByClientPhoneOrderByCreatedAtDesc(String clientPhone);
    int countByClientPhoneAndIsReadFalse(String clientPhone);
}
