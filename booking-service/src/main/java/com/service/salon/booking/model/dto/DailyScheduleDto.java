package com.service.salon.booking.model.dto;

import com.service.salon.booking.model.Booking;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DailyScheduleDto {
    private List<Booking> bookings;
    private String firstClientTime;
    private String lastClientTime;
    private int totalBusyMinutes;
}
