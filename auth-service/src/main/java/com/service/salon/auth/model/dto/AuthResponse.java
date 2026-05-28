package com.service.salon.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType;
    private String role;

    private String firstName;
    private String phoneNumber;
    private String email;
}
