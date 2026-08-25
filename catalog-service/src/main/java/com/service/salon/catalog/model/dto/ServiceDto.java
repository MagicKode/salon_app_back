package com.service.salon.catalog.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
    private Long categoryId;
    private Integer sortOrder;
    private ImageDto image;
    private Boolean isActive;
}
