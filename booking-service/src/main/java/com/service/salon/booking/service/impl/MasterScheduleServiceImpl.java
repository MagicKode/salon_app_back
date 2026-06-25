package com.service.salon.booking.service.impl;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.dto.DailyScheduleDto;
import com.service.salon.booking.model.dto.DayStatDto;
import com.service.salon.booking.repository.BookingRepository;
import com.service.salon.booking.service.MasterScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterScheduleServiceImpl implements MasterScheduleService {

    private  final BookingRepository bookingRepository;

    @Override
    @Cacheable(value = "masterSchedule", key = "#masterName + '_today'")
    public DailyScheduleDto getTodaySchedule(String masterName) {
        LocalDate today = LocalDate.now();

        List<Booking> todayBookings = bookingRepository.findByMasterNameAndBookingDateOrderByBookingTimeAsc(masterName, today);

        if (todayBookings.isEmpty()) {
            return DailyScheduleDto.builder()
                    .bookings(Collections.emptyList())
                    .firstClientTime("-")
                    .lastClientTime("-")
                    .totalBusyMinutes(0)
                    .build();
        }

        // 3. Вычисляем первого клиента (проверка на null на всякий случай)
        LocalTime firstTimeObj = todayBookings.get(0).getBookingTime();
        String firstTime = firstTimeObj != null ? firstTimeObj.toString() : "-";

        // 4. Вычисляем последнего клиента (время начала + длительность в минутах)
        Booking lastBooking = todayBookings.get(todayBookings.size() - 1);
        LocalTime lastTimeObj = lastBooking.getBookingTime();
        String lastTime = "-";

        if (lastTimeObj != null && lastBooking.getDurationMinutes() != null) {
            lastTime = lastTimeObj.plusMinutes(lastBooking.getDurationMinutes()).toString();
        }

        // 5. Просто суммируем готовые минуты из БД
        int totalMinutes = todayBookings.stream()
                .mapToInt(b -> b.getDurationMinutes() != null ? b.getDurationMinutes() : 0)
                .sum();

        // 3. Собираем и возвращаем DTO
        return DailyScheduleDto.builder()
                .bookings(todayBookings)
                .firstClientTime(firstTime)
                .lastClientTime(lastTime)
                .totalBusyMinutes(totalMinutes)
                .build();

    }

    @Override
    @Cacheable(value = "monthStats", key = "#masterName + '_' + #year + '_' + #month")
    public List<DayStatDto> getMonthlyStats(String masterName, int month, int year) {
        return bookingRepository.getMonthlyStats(masterName, month, year);
    }

    @Override
    @Cacheable(value = "monthAppointments", key = "#masterName + '_' + #year + '_' + #month")
    public List<Booking> getMonthAppointments(String masterName, int month, int year) {
        return bookingRepository.getMonthAppointments(masterName, month, year);
    }
}
