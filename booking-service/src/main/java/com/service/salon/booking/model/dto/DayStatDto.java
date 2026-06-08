package com.service.salon.booking.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DayStatDto {

    @JsonProperty("date")
    private LocalDate bookingDate;
    private long bookingCount;

    @JsonProperty("status")
    public String getStatus() {
        // Если записей больше 2 (например), то день FULL, иначе AVAILABLE
        return this.bookingCount >= 2 ? "FULL" : "AVAILABLE";
    }
}
