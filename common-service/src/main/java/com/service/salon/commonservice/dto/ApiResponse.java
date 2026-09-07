package com.service.salon.commonservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Универсальная обертка для ответов API")
public class ApiResponse<T> {

    @Schema(description = "Флаг успешности операции", example = "true")
    private boolean success;

    @Schema(description = "Сообщение (заполняется при ошибках)", example = "Операция выполнена успешно")
    private String message;

    @Schema(description = "Полезная нагрузка (тело ответа)")
    private T data;

    @Schema(description = "Таймштамп ответа")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // Быстрый хелпер для успешного ответа
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    // Быстрый хелпер для ошибки
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
