package com.service.salon.booking.service;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.dto.TimeSlotDto;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking, String username);

    List<Booking> getClientHistory(String clientName);
    List<Booking> getBookingsByMasterAndDate(String masterName, LocalDate localDate);
    List<TimeSlotDto> getAvailableSlots(String masterName, LocalDate date);
    List<LocalDate> getFullyBusyDates(String masterName, int month, int year);
}
