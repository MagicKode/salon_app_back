package com.service.salon.booking.repository;

import com.service.salon.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Поиск всех записей к конкретному мастеру на выбранный день
    List<Booking> findByMasterNameAndBookingDate(String masterName, LocalDate date);

    // Поиск истории записей для конкретного клиента
    List<Booking> findByClientNameOrderByBookingDateDescBookingTimeDesc(String clientName);
}
