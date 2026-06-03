package com.service.salon.booking.controller;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.dto.TimeSlotDto;
import com.service.salon.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody Booking booking,
            @RequestHeader(value = "X-User-Name") String username
    ) {
        try {
            Booking savedBooking = bookingService.createBooking(booking, username);
            return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при создании бронирования");
        }
    }

    // Получить историю записей клиента (для третьей вкладки, image_8ad29d.png)
    @GetMapping("/history")
    public ResponseEntity<List<Booking>> getHistory(
            @RequestHeader(value = "X-User-Name") String username
    ) {
        List<Booking> history = bookingService.getClientHistory(username);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/slots")
    public ResponseEntity<List<TimeSlotDto>> getAvailableSlots(
            @RequestParam String masterName,
            @RequestParam String date) {
        return ResponseEntity.ok(bookingService.getAvailableSlots(masterName, LocalDate.parse(date)));
    }

    @GetMapping("/busy-dates")
    public ResponseEntity<List<LocalDate>> getFullyBusyDates(
            @RequestParam String masterName,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(bookingService.getFullyBusyDates(masterName, month, year));
    }
}
