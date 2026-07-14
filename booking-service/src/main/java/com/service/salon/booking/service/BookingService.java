package com.service.salon.booking.service;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.BookingStatus;
import com.service.salon.booking.model.dto.BookingRequestDto;
import com.service.salon.booking.model.dto.TimeSlotDto;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    Booking createBooking(BookingRequestDto bookingRequestDto, String username);
    void cancelBooking(Long bookingId, String username);
    void updateBookingComment(Long bookingId, String newComment, String username);

    List<Booking> getBookingsByMasterAndDate(String masterName, LocalDate localDate, BookingStatus status);
    List<TimeSlotDto> getAvailableSlots(String masterName, LocalDate date,  BookingStatus status);
    List<LocalDate> getFullyBusyDates(String masterName, int month, int year);
}
