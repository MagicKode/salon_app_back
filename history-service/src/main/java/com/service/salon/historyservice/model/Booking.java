package com.service.salon.historyservice.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name", nullable = false)
    private String clientName; // В будущем привяжем к User ID

    @Column(name = "master_name", nullable = false)
    private String masterName; // На основе макета "Pavel"

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate; // Например, 2026-05-31

    @Column(name = "booking_time", nullable = false)
    private LocalTime bookingTime; // Например, 13:00:00

    @ElementCollection
    @CollectionTable(name = "booking_services", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "service_name")
    private List<String> serviceNames;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;
}
