package com.service.salon.booking.service.impl;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.BookingStatus;
import com.service.salon.booking.model.dto.BookingRequestDto;
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
            "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
    );

    @Transactional
    @Override
    public Booking createBooking(BookingRequestDto bookingRequestDto, String username) {
        bookingRequestDto.setClientName(username);
        bookingRequestDto.setMasterName("Pavel");

        // 1. Вычисляем, сколько часов (слотов) займет сеанс по количеству услуг
        int durationHours = calculateDurationHours(bookingRequestDto.getServiceNames());
        bookingRequestDto.setDurationMinutes(durationHours * 60);

        LocalTime startTime = bookingRequestDto.getBookingTime();
        LocalTime endTime = startTime.plusHours(durationHours);

        // 2. Ищем все брони мастера на эту дату
        List<Booking> existingBookings = bookingRepository.findByMasterNameAndBookingDateAndStatus(
                bookingRequestDto.getMasterName(),
                bookingRequestDto.getBookingDate(),
                BookingStatus.CONFIRMED
        );

        // 3. Проверяем пересечение временных интервалов
        boolean isIntervalOverlapped = existingBookings.stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELED)
                .anyMatch(b -> {
                    LocalTime existStart = b.getBookingTime();
                    int existDuration = b.getDurationMinutes() != null ?
                            (int) Math.ceil(b.getDurationMinutes() / 60.0) : 1;
                    LocalTime existEnd = existStart.plusHours(existDuration);
                    return startTime.isBefore(existEnd) && endTime.isAfter(existStart);
                });

        if (isIntervalOverlapped) {
            throw new IllegalStateException("Извините, время уже занято!");
        }

        Booking booking = new Booking();
        booking.setClientName(bookingRequestDto.getClientName());
        booking.setMasterName(bookingRequestDto.getMasterName());
        booking.setBookingDate(bookingRequestDto.getBookingDate());
        booking.setBookingTime(bookingRequestDto.getBookingTime());
        booking.setServiceNames(bookingRequestDto.getServiceNames());
        booking.setTotalPrice(bookingRequestDto.getTotalPrice());
        booking.setNotes(bookingRequestDto.getNotes());
        booking.setDurationMinutes(bookingRequestDto.getDurationMinutes());
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getClientHistory(String username) {
        return bookingRepository.findByClientNameOrderByBookingDateDescBookingTimeDesc(username);
    }

    @Override
    public List<Booking> getBookingsByMasterAndDate(String masterName, LocalDate localDate, BookingStatus status) {
        return bookingRepository.findByMasterNameAndBookingDateAndStatus(masterName, localDate, status);
    }

    @Override
    public List<TimeSlotDto> getAvailableSlots(String masterName, LocalDate date, BookingStatus status) {
        // 1. Получаем активные брони мастера
        List<Booking> activeBookings = bookingRepository.
                findByMasterNameAndBookingDateAndStatus(masterName.trim(), date, status);

        // 2. Идем по каждому 30-минутному слоту
        return WORK_SLOTS.stream().map(slotStr -> {
            LocalTime targetTime = LocalTime.parse(slotStr);

            boolean isBusy = activeBookings.stream().anyMatch(booking -> {
                LocalTime bookingStart = booking.getBookingTime();
                if (bookingStart == null) return false;

                int durationHours = booking.getDurationMinutes() != null ?
                        (int) Math.ceil(booking.getDurationMinutes() / 60.0) : 1;
                LocalTime bookingEnd = bookingStart.plusHours(durationHours);

                // Слот закрывается, если targetTime находится в полуинтервале [bookingStart, bookingEnd)
                return (!targetTime.isBefore(bookingStart)) && targetTime.isBefore(bookingEnd);
            });

            return new TimeSlotDto(slotStr, !isBusy);
        }).toList();
    }

    @Override
    public List<LocalDate> getFullyBusyDates(String masterName, int month, int year) {
        return List.of();
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId, String username) {
        // 1. Ищем бронь в базе
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено"));

        // 2. Безопасность: проверяем, что эту бронь отменяет именно тот клиент, который её создал
        if (!booking.getClientName().equalsIgnoreCase(username)) {
            throw new IllegalArgumentException("Вы не можете отменить чужое бронирование!");
        }

        // 3. Меняем статус на отменённый
        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void updateBookingComment(Long bookingId, String newComment, String username) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено"));

        // 2. Безопасность: проверят только автор брони
        if (!booking.getClientName().equalsIgnoreCase(username)) {
            throw new IllegalStateException("Вы не можете изменять комментарий к чужому бронированию!");
        }

        // 3. Обновляем комментарий и сохраняем
        booking.setNotes(newComment); // Предполагаем, что у сущности Booking есть поле comment
        bookingRepository.save(booking);
    }

    private int calculateDurationHours(List<String> services) {
        if (services == null || services.isEmpty()) {
            return 1;
        }
        return services.size();
    }

    private int getDurationInHours(BookingRequestDto bookingRequestDto) {
        // Если пришло 0 или null, считаем как 1 час
        if (bookingRequestDto.getDurationMinutes() == null || bookingRequestDto.getDurationMinutes() <= 0) {
            return 1;
        }
        // Округляем вверх (например, 90 мин = 2 часа, 120 мин = 2 часа)
        return (int) Math.ceil(bookingRequestDto.getDurationMinutes() / 60.0);
    }

}
