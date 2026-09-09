package com.service.salon.catalog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterDto {
    private Long id;
    private String name;
    private String position;
    private String description;
    private ImageDto image; // фото
    private Boolean isActive;
}
