package com.service.salon.auth.service;

import com.service.salon.auth.model.UserEntity;
import com.service.salon.auth.model.dto.AuthResponse;
import com.service.salon.auth.model.dto.LoginRequest;
import com.service.salon.auth.model.dto.RegisterRequest;
import com.service.salon.auth.model.dto.UpdateProfileRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserEntity getUserByPhone(String phoneNumber);
    void updateProfile(String phoneNumber, UpdateProfileRequest request);
}
