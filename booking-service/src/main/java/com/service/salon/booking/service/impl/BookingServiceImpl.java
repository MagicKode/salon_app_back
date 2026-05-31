package com.service.salon.booking.service.impl;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.BookingStatus;
import com.service.salon.booking.repository.BookingRepository;
import com.service.salon.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public Booking createBooking(Booking booking, String username) {
        booking.setClientName(username);

        List<Booking> existingBookings = bookingRepository.findByMasterNameAndBookingDate(
                booking.getMasterName(),
                booking.getBookingDate()
        );

        boolean isSlotBusy = existingBookings.stream()
                .anyMatch(b -> b.getBookingTime().equals(b.getBookingTime()) && b.getStatus() != BookingStatus.CANCELED);

        if (isSlotBusy) {
            throw new IllegalStateException("Извините, это время уже занято другим клиентом!");
        }

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getClientHistory(String username) {
        return bookingRepository.findByClientNameOrderByBookingDateDescBookingTimeDesc(username);
    }

    @Override
    public List<Booking> getBookingsByMasterAndDate(String masterName, LocalDate localDate) {
        return bookingRepository.findByMasterNameAndBookingDate(masterName, localDate);
    }
}
