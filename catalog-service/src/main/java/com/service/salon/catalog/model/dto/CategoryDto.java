package com.service.salon.catalog.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private ImageDto image;
    private Integer sortOrder;
}
