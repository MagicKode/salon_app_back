package com.service.salon.auth.service;

import com.service.salon.auth.model.dto.AuthResponse;
import com.service.salon.auth.model.dto.LoginRequest;
import com.service.salon.auth.model.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
