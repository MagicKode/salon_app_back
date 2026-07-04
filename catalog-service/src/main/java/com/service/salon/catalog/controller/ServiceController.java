package com.service.salon.catalog.controller;

import com.service.salon.catalog.model.ServiceEntity;
import com.service.salon.catalog.model.dto.ImageDto;
import com.service.salon.catalog.model.dto.ServiceDto;
import com.service.salon.catalog.repository.ImageRepository;
import com.service.salon.catalog.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceRepository serviceRepository;
    private final ImageRepository imageRepository;



    @GetMapping
    public ResponseEntity<List<ServiceDto>> getActiveServices(){
        List<ServiceDto> list = serviceRepository.findByActiveTrueOrderBySortOrderAsc()
                .stream().map(this::toDto).toList();
        return ResponseEntity.ok(list);
    }

    // Услуги конкретной категории
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ServiceDto>> getServicesByCategory(@PathVariable Long categoryId) {
        List<ServiceDto> list = serviceRepository.findByCategoryIdAndActiveTrueOrderBySortOrderAsc(categoryId)
                .stream().map(this::toDto).toList();
        return ResponseEntity.ok(list);
    }

    private ServiceDto toDto(ServiceEntity entity) {
        ImageDto imageDto = null;
        if (entity.getImageId() != null) {
            imageDto = imageRepository.findById(Long.valueOf(entity.getImageId()))
                    .map(img -> ImageDto.builder()
                            .id(img.getId())
                            .url("/api/v1/catalog/images/" + img.getId())
                            .contentType(img.getContentType())
                            .originalName(img.getOriginalName())
                            .createdAt(img.getCreatedAt())
                            .build())
                    .orElse(null);
        }

        Long categoryId = entity.getCategory() != null ? entity.getCategory().getId() : null;

        return ServiceDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .durationMinutes(entity.getDurationMinutes())
                .categoryId(categoryId)
                .sortOrder(entity.getSortOrder())
                .image(imageDto)
                .build();
    }
}
