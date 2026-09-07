package com.service.salon.booking.repository;

import com.service.salon.booking.model.Booking;
import com.service.salon.booking.model.BookingStatus;
import com.service.salon.booking.model.dto.DayStatDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Поиск всех записей к конкретному мастеру на выбранный день
    List<Booking> findByMasterNameAndBookingDateAndStatus(String masterName, LocalDate date, BookingStatus status);

    // Поиск истории записей для конкретного клиента
    List<Booking> findByClientNameOrderByBookingDateDescBookingTimeDesc(String clientName);

    // Для экрана "Календарь на месяц"
    @Query("SELECT new com.service.salon.booking.model.dto.DayStatDto(b.bookingDate, COUNT(b)) " +
            "FROM Booking b " +
            "WHERE b.masterName = :masterName " +
            "AND MONTH(b.bookingDate) = :month " +
            "AND YEAR(b.bookingDate) = :year " +
            "GROUP BY b.bookingDate")
    List<DayStatDto> getMonthlyStats(@Param("masterName") String masterName,
                                     @Param("month") int month,
                                     @Param("year") int year);

    List<Booking> findByMasterNameAndBookingDateOrderByBookingTimeAsc(String masterName, LocalDate bookingDate);

    // Допиши этот метод в интерфейс репозитория:
    @Query("SELECT b FROM Booking b " +
            "WHERE b.masterName = :masterName " +
            "AND MONTH(b.bookingDate) = :month " +
            "AND YEAR(b.bookingDate) = :year")
    List<Booking> getMonthAppointments(@Param("masterName") String masterName,
                                       @Param("month") int month,
                                       @Param("year") int year);
}
