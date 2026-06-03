package com.service.salon.historyservice.service.impl;

import com.service.salon.historyservice.model.Booking;
import com.service.salon.historyservice.repository.BookingHistoryRepository;
import com.service.salon.historyservice.service.HistoryService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final BookingHistoryRepository bookingHistoryRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getClientHistory(String token, String userPhone) {

        // 1. Проверяем токен
        validateJwtToken(token);

        return bookingHistoryRepository.findByClientNameOrderByBookingDateDescBookingTimeDesc(userPhone);
    }

    private void validateJwtToken(String token) {
        try {
            String pureToken = token.replace("Bearer ", "");

            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(pureToken);

        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка авторизации: Невалидный или просроченный JWT токен");
        }
    }
}

