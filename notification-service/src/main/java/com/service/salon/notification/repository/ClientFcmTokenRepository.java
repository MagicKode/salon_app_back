package com.service.salon.notification.repository;

import com.service.salon.notification.model.ClientFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientFcmTokenRepository extends JpaRepository<ClientFcmToken, String> {
    Optional<ClientFcmToken> findByPhoneNumber(String phoneNumber);
}
