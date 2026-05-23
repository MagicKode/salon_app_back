package com.service.salon.catalog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salon")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Double latitude;        // Координаты для Flutter-карты

    @Column(nullable = false)
    private Double longitude;       // Координаты для Flutter-карты

    @Column(name = "phone_number")
    private String phoneNumber;

    private Double rating;

    @Column(name = "working_hours")
    private String workingHours;
}
