package com.service.salon.catalog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalonDto {
    private Long id;
    private String name;
    private String address;
    private String description;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private Double rating;
    private String workingHours;
}
