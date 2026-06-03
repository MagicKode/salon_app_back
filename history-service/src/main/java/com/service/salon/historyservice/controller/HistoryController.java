package com.service.salon.historyservice.controller;

import com.service.salon.historyservice.model.Booking;
import com.service.salon.historyservice.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<?> getClientHistory(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestHeader(value = "X-User-Name", required = false) String userPhone) {

        // Валидация наличия заголовков прямо на входе
        if (token == null || userPhone == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Отсутствуют обязательные заголовки авторизации");
        }

        try {
            List<Booking> history = historyService.getClientHistory(token, userPhone);
            return ResponseEntity.ok(history);
        } catch (IllegalArgumentException e) {
            // Ошибка валидации токена (бизнес-логика)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            // Перехват любой 500-й ошибки БД
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка сервера: " + e.getMessage());
        }
    }
}
