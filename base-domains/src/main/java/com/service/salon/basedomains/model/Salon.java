package com.service.salon.basedomains.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Salon {
    private Long id;
    private String name;
    private String address;
    private String description;
    private Double latitude;        // Координаты для Flutter-карты
    private Double longitude;       // Координаты для Flutter-карты
    private String phoneNumber;
    private Double rating;
    private String workingHours;
}
