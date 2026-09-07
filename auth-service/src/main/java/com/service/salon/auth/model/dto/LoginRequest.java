package com.service.salon.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Пожалуйста, введите номер телефона")
    private String phoneNumber;

    @NotBlank(message = "Пожалуйста, введите пароль")
    private String password;
}
