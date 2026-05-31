package com.service.salon.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientName; // В будущем привяжем к User ID

    @Column(nullable = false)
    private String masterName; // На основе макета "Pavel"

    @Column(nullable = false)
    private LocalDate bookingDate; // Например, 2026-05-31

    @Column(nullable = false)
    private LocalTime bookingTime; // Например, 13:00:00

    @ElementCollection
    @CollectionTable(name = "booking_services", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "service_name")
    private List<String> serviceNames;

    private BigDecimal totalPrice;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

}
