package com.service.salon.booking.service;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.dto.DailyScheduleDto;
import com.service.salon.booking.model.dto.DayStatDto;

import java.util.List;

public interface MasterScheduleService {
    DailyScheduleDto getTodaySchedule(String masterName);
    List<DayStatDto> getMonthlyStats(String masterName, int month, int year);
    List<Booking> getMonthAppointments(String masterName, int month, int year);
}
