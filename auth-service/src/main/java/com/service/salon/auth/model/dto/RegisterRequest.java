package com.service.salon.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Пожалуйста, введите номер телефона")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Неверный формат номера телефона")
    private String phoneNumber;

    @NotBlank(message = "Пожалуйста, введите пароль")
    private String password;

    private String firstName;
    private String email;
}
