package com.service.salon.catalog.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private ImageDto image;
    private Integer sortOrder;
}
