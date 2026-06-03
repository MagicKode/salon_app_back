package com.service.salon.booking.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDto {
    @JsonProperty("time")
    private String time;

    @JsonProperty("isAvailable")
    private boolean isAvailable;

    @JsonProperty("duration_minutes")
    private Integer durationMinutes;

    // ДОБАВЛЯЕМ КАСТОМНЫЙ КОНСТРУКТОР
    public TimeSlotDto(String time, boolean isAvailable) {
        this.time = time;
        this.isAvailable = isAvailable;
        this.durationMinutes = null; // или 60
    }
}
