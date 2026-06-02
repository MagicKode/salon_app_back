package com.service.salon.booking.service.impl;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.BookingStatus;
import com.service.salon.booking.model.dto.TimeSlotDto;
import com.service.salon.booking.repository.BookingRepository;
import com.service.salon.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    private final List<String> WORK_SLOTS = List.of(
            "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
    );

    @Transactional
    @Override
    public Booking createBooking(Booking booking, String username) {
        booking.setClientName(username);

        List<Booking> existingBookings = bookingRepository.findByMasterNameAndBookingDate(
                booking.getMasterName(),
                booking.getBookingDate()
        );

        boolean isIntervalOverlapped = existingBookings.stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELED)
                .anyMatch(b -> {
                    LocalTime existStart = b.getBookingTime();
                    LocalTime existEnd = existStart.plusHours(1);

                    LocalTime newStart = booking.getBookingTime();
                    // Предполагаем дефолтную длительность в 1 час, либо динамически вычисляем (newStart.plusHours(requiredSlots))
                    LocalTime newEnd = newStart.plusHours(1);

                    return newStart.isBefore(existEnd) && newEnd.isAfter(existStart);
                });

        if (isIntervalOverlapped) {
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

    @Override
    public List<TimeSlotDto> getAvailableSlots(String masterName, LocalDate date) {
        System.out.println("=== [GET SLOTS] Запрос для мастера: " + masterName + ", Дата: " + date);

        // 2. Ищем в БД записи строго для этого мастера и этой даты
        List<Booking> activeBookings = bookingRepository.findByMasterNameAndBookingDate(masterName.trim(), date).stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELED)
                .toList();
        ;

        System.out.println("Найдено активных записей в БД на русском: " + activeBookings.size());

        // Логирование для контроля
        System.out.println("=== [GET SLOTS] Мастер: " + masterName + ", Дата: " + date + " ===");
        System.out.println("Найдено активных записей в БД: " + activeBookings.size());

        return WORK_SLOTS.stream().map(slotStr -> {
            LocalTime targetTime = LocalTime.parse(slotStr);

            boolean isBusy = activeBookings.stream().anyMatch(booking -> {
                LocalTime bookingStart = booking.getBookingTime();
                if (bookingStart == null) return false;

                // Вычисляем, сколько слотов (часов) занимает это бронирование.
                // FIX: Считаем количество услуг. Если услуг больше, берем их количество за часы.
                // Если у тебя есть явное поле длительности, используй его: int durationHours = booking.getDurationHours();
                int durationHours = (booking.getServiceNames() != null && !booking.getServiceNames().isEmpty())
                        ? booking.getServiceNames().size()
                        : 1;

                LocalTime bookingEnd = bookingStart.plusHours(durationHours);

                // Текущий слот занят, если он находится в интервале [bookingStart, bookingEnd)
                return (!targetTime.isBefore(bookingStart)) && targetTime.isBefore(bookingEnd);
            });

            return new TimeSlotDto(slotStr, !isBusy);
        }).toList();
    }

    @Override
    public List<LocalDate> getFullyBusyDates(String masterName, int month, int year) {
        return List.of();
    }
}
