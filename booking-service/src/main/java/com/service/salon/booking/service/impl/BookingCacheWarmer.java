package com.service.salon.booking.service.impl;

import com.service.salon.booking.model.BookingStatus;
import com.service.salon.booking.service.BookingService;
import com.service.salon.booking.service.MasterScheduleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCacheWarmer {

    private final MasterScheduleService masterScheduleService;
    private final BookingService bookingService;

    @PostConstruct
    public void warmUpBookingCaches() {
        try {
            LocalDate today = LocalDate.now();
            // Прогреваем слоты на сегодня для мастера Pavel
            bookingService.getAvailableSlots("Pavel", today, BookingStatus.CONFIRMED);
            // Прогреваем расписание на сегодня
            masterScheduleService.getTodaySchedule("Pavel");
            // Прогреваем статистику месяца
            masterScheduleService.getMonthlyStats("Pavel", today.getMonthValue(), today.getYear());
            masterScheduleService.getMonthAppointments("Pavel", today.getMonthValue(), today.getYear());

            log.info("✅ Booking caches (slots, today schedule, monthly stats) warmed up");
        } catch (Exception e) {
            log.warn("⚠️ Could not warm up booking caches: {}", e.getMessage());
        }
    }
}