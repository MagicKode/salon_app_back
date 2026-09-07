package com.service.salon.notification.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client_fcm_tokens")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClientFcmToken {
    @Id
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "fcm_token", nullable = false)
    private String fcmToken;
}
