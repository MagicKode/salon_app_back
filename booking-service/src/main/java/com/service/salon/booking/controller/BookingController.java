package com.service.salon.booking.controller;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.BookingStatus;
import com.service.salon.booking.model.dto.BookingRequestDto;
import com.service.salon.booking.model.dto.CommentRequestDto;
import com.service.salon.booking.model.dto.TimeSlotDto;
import com.service.salon.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
            @RequestBody BookingRequestDto bookingRequestDto,
            @RequestHeader(value = "X-User-Name") String username
    ) {
        try {
            Booking savedBooking = bookingService.createBooking(bookingRequestDto, username);
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
            @RequestParam String date,
            @RequestParam String status
            ) {
        BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(bookingService.getAvailableSlots(masterName, LocalDate.parse(date), bookingStatus));
    }

    @GetMapping("/busy-dates")
    public ResponseEntity<List<LocalDate>> getFullyBusyDates(
            @RequestParam String masterName,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(bookingService.getFullyBusyDates(masterName, month, year));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(
            @PathVariable Long id,
            @RequestHeader("X-User-Name") String username) {
        try {
            bookingService.cancelBooking(id, username);
            return ResponseEntity.ok("Бронирование успешно отменено");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при отмене");
        }
    }

    @PatchMapping("/{bookingId}/comment")
    public ResponseEntity<?> updateBookingComment(
            @PathVariable Long bookingId,
            @RequestBody CommentRequestDto commentDto,
            @RequestHeader("X-User-Name") String username) {
        try {
            bookingService.updateBookingComment(bookingId, commentDto.getComment(), username);
            return ResponseEntity.ok("Комментарий успешно обновлен");
        } catch (IllegalStateException e) {
            // Сюда упадет ошибка, если X-User-Name не совпал с автором брони
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // Сюда упадет ошибка, если бронь с таким ID не найдена в базе
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении комментария");
        }
    }
}
