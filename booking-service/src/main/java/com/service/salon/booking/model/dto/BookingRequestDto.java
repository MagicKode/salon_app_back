package com.service.salon.booking.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingRequestDto {
    private String clientName;
    private String masterName;
    private String clientPhone;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private List<String> serviceNames;
    private BigDecimal totalPrice;
    private String notes;
    private Integer durationMinutes;
}
