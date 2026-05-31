package com.service.salon.booking.service;

import com.service.salon.booking.model.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking, String username);
    List<Booking> getClientHistory(String clientName);

    List<Booking> getBookingsByMasterAndDate(String masterName, LocalDate localDate);
}
