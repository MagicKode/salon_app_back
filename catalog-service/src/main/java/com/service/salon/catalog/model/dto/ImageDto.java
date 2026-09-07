package com.service.salon.catalog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {
    private Long id;
    private String url;  // относительный путь, например "/api/v1/catalog/images/123"
    private String relatedType;
    private Long relatedId;
    private String contentType;
    private String originalName;
    private LocalDateTime createdAt;
}
