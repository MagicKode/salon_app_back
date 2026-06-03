package com.service.salon.historyservice.repository;

import com.service.salon.historyservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingHistoryRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByClientNameOrderByBookingDateDescBookingTimeDesc(String clientName);
}
