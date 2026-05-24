package com.service.salon.auth.service.impl;

import com.service.salon.auth.model.Role;
import com.service.salon.auth.model.UserEntity;
import com.service.salon.auth.model.dto.AuthResponse;
import com.service.salon.auth.model.dto.LoginRequest;
import com.service.salon.auth.model.dto.RegisterRequest;
import com.service.salon.auth.repository.UserRepository;
import com.service.salon.auth.service.AuthService;
import com.service.salon.auth.util.JwtUtil;
import com.service.salon.commonservice.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Регистрация нового пользователя по номеру телефона: {}", request.getPhoneNumber());

        if (userRepository.existsByUsername(request.getPhoneNumber())) {
            throw new BusinessException("Пользователь с таким номером телефона уже зарегистрирован", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = UserEntity.builder()
                .username(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword())) // Хэшируем
                .role(Role.CLIENT) // По умолчанию регистрируем как клиента
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, "Bearer", user.getRole().name());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Попытка входа пользователя по номеру телефона: {}", request.getPhoneNumber());

        UserEntity user = userRepository.findByUsername(request.getPhoneNumber())
                .orElseThrow(() -> new BusinessException("Неверный номер телефона или пароль", HttpStatus.UNAUTHORIZED));

        // Проверяем хэши паролей
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Неверный номер телефона или пароль", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, "Bearer", user.getRole().name());
    }
}
