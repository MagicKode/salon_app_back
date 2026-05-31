package com.service.salon.booking.controller;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.BookingStatus;
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
            @RequestHeader("X-User-Name") String username
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
    public ResponseEntity<List<Booking>> getHistory(@RequestHeader("X-User-Name") String username) {
        List<Booking> history = bookingService.getClientHistory(username);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/slots")
    public ResponseEntity<List<TimeSlotDto>> getAvailableSlots(
            @RequestParam String masterName,
            @RequestParam String date) { // Передаем дату строкой "2026-05-31"

        LocalDate localDate = LocalDate.parse(date);

        // Массив всех стандартных рабочих слотов (как на твоем макете)
        List<String> workSlots = List.of(
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "19:00"
        );

        // Получаем из базы то, что уже забронировано
        List<Booking> activeBookings = bookingService.getBookingsByMasterAndDate(masterName, localDate);

        // Сопоставляем и размечаем слоты
        List<TimeSlotDto> slots = workSlots.stream().map(slotTime -> {
            boolean isBusy = activeBookings.stream()
                    .anyMatch(b -> b.getBookingTime().toString().startsWith(slotTime)
                            && b.getStatus() != BookingStatus.CANCELED);
            return new TimeSlotDto(slotTime, !isBusy);
        }).toList();

        return ResponseEntity.ok(slots);
    }
}
