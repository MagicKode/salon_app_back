package com.service.salon.auth.controller;

import com.service.salon.auth.model.UserEntity;
import com.service.salon.auth.model.dto.AuthResponse;
import com.service.salon.auth.model.dto.LoginRequest;
import com.service.salon.auth.model.dto.RegisterRequest;
import com.service.salon.auth.model.dto.UpdateProfileRequest;
import com.service.salon.auth.service.AuthService;
import com.service.salon.auth.util.JwtUtil;
import com.service.salon.commonservice.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse authResponse = authService.register(registerRequest);
        return new ResponseEntity<>(ApiResponse.success(authResponse), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return new ResponseEntity<>(ApiResponse.success(response), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<AuthResponse>> getProfile(@RequestHeader("Authorization") String token) {
        // Извлекаем phoneNumber из токена (у вас уже есть JwtUtil)
        String phoneNumber = jwtUtil.extractPhoneNumber(token.replace("Bearer ", ""));
        UserEntity user = authService.getUserByPhone(phoneNumber);

        AuthResponse profile = new AuthResponse(
                null,
                null,
                user.getRole().name(),
                user.getFirstName(),
                user.getPhoneNumber(),
                user.getEmail()
        );
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateProfileRequest request) {

        String phoneNumber = jwtUtil.extractPhoneNumber(token.replace("Bearer ", ""));
        authService.updateProfile(phoneNumber, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
