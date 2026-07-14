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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final BookingHistoryRepository bookingHistoryRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public List<Booking> getActiveHistory(String token, String userPhone) {
        validateJwtToken(token);
        List<Booking> all = bookingHistoryRepository
                .findByClientPhoneOrderByBookingDateDescBookingTimeDesc(userPhone);
        LocalDateTime now = LocalDateTime.now();
        return all.stream()
                .filter(b -> {
                    LocalDateTime endTime = b.getBookingDate().atTime(b.getBookingTime())
                            .plusMinutes(b.getDurationMinutes() != null ? b.getDurationMinutes() : 60);
                    return endTime.isAfter(now);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getPastHistory(String token, String userPhone) {
        validateJwtToken(token);
        List<Booking> all = bookingHistoryRepository
                .findByClientPhoneOrderByBookingDateDescBookingTimeDesc(userPhone);
        LocalDateTime now = LocalDateTime.now();
        return all.stream()
                .filter(b -> {
                    LocalDateTime endTime = b.getBookingDate().atTime(b.getBookingTime())
                            .plusMinutes(b.getDurationMinutes() != null ? b.getDurationMinutes() : 60);
                    return endTime.isBefore(now);
                })
                .collect(Collectors.toList());
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

