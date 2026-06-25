package com.service.salon.auth.service.impl;

import com.service.salon.auth.model.Role;
import com.service.salon.auth.model.UserEntity;
import com.service.salon.auth.model.dto.AuthResponse;
import com.service.salon.auth.model.dto.LoginRequest;
import com.service.salon.auth.model.dto.RegisterRequest;
import com.service.salon.auth.model.dto.UpdateProfileRequest;
import com.service.salon.auth.repository.UserRepository;
import com.service.salon.auth.service.AuthService;
import com.service.salon.auth.util.JwtUtil;
import com.service.salon.commonservice.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JavaMailSender mailSender;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Регистрация нового пользователя по номеру телефона: {}", request.getPhoneNumber());

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BusinessException("Пользователь с таким номером телефона уже зарегистрирован", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = UserEntity.builder()
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword())) // Хэшируем
                .role(Role.CLIENT) // По умолчанию регистрируем как клиента
                .firstName(request.getFirstName())
                .email(request.getEmail())
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getPhoneNumber(), user.getRole().name());
        return new AuthResponse(
                token,
                "Bearer",
                user.getRole().name(),
                user.getFirstName(),
                user.getPhoneNumber(),
                user.getEmail()
        );
    }

    @Override
    @Cacheable(value = "userProfile", key = "#phoneNumber")
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Попытка входа пользователя по номеру телефона: {}", request.getPhoneNumber());

        UserEntity user = getUserByPhone(request.getPhoneNumber());

        // Проверяем хэши паролей
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Неверный номер телефона или пароль", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(user.getPhoneNumber(), user.getRole().name());
        return new AuthResponse(
                token,
                "Bearer",
                user.getRole().name(),
                user.getFirstName(),
                user.getPhoneNumber(),
                user.getEmail() != null ? user.getEmail() : "Email не указан"
        );
    }

    @Cacheable(value = "userProfile", key = "#phoneNumber")
    @Transactional(readOnly = true)
    public UserEntity getUserByPhone(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new BusinessException("Неверный номер телефона или пароль", HttpStatus.UNAUTHORIZED));
    }

    @Override
    @Transactional
    @CacheEvict(value = "userProfile", key = "#phoneNumber")
    public void updateProfile(String phoneNumber, UpdateProfileRequest request) {
        UserEntity user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new BusinessException("Пользователь не найден", HttpStatus.NOT_FOUND));
        user.setFirstName(request.getFirstName());
        user.setEmail(request.getEmail());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void sendResetCode(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Пользователь с таким email не найден", HttpStatus.NOT_FOUND));

        String code = String.format("%06d", new Random().nextInt(999999));
        user.setResetCode(code);
        user.setResetCodeExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Код для сброса пароля");
        message.setText("Ваш код подтверждения: " + code + "\nДействителен 15 минут.");
        log.info("Generated reset code for {}: {}", email, code);

        mailSender.send(message);

        log.info("Reset code sent to {}", email);
    }

    @Override
    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Пользователь не найден", HttpStatus.NOT_FOUND));

        log.info("Comparing codes: stored='{}' (length={}), received='{}' (length={})",
                user.getResetCode(), user.getResetCode() == null ? 0 : user.getResetCode().length(),
                code, code.length());

        if (user.getResetCode() == null || !user.getResetCode().trim().equals(code.trim())) {
            throw new BusinessException("Неверный код", HttpStatus.BAD_REQUEST);
        }
        if (user.getResetCodeExpiry() == null || user.getResetCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Срок действия кода истёк", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetCode(null);
        user.setResetCodeExpiry(null);
        userRepository.save(user);

        log.info("Stored reset code: '{}', received code: '{}'", user.getResetCode(), code);
    }
}
