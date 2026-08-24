package com.service.salon.catalog.service.impl;

import com.service.salon.catalog.model.ServiceEntity;
import com.service.salon.catalog.model.dto.ImageDto;
import com.service.salon.catalog.model.dto.ServiceDto;
import com.service.salon.catalog.repository.ImageRepository;
import com.service.salon.catalog.repository.ServiceRepository;
import com.service.salon.catalog.service.ServiceCacheService;
import com.service.salon.commonservice.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCacheServiceImpl implements ServiceCacheService {

    private final ServiceRepository serviceRepository;
    private final ImageRepository imageRepository;

    @Override
    @Cacheable(value = "services", key = "'all_active'")
    public List<ServiceDto> getActiveServices() {
        return serviceRepository.findByIsActiveTrueOrderBySortOrderAsc()
                .stream().map(this::toDto).toList();
    }

    @Override
    @Cacheable(value = "services", key = "'category_' + #categoryId")
    public List<ServiceDto> getServicesByCategory(Long categoryId) {
        return serviceRepository.findByCategoryIdAndIsActiveTrueOrderBySortOrderAsc(categoryId)
                .stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public ServiceEntity updateDescription(Long id, String newDescription) {
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Услуга не найдена"));

        entity.setDescription(newDescription);

        return serviceRepository.save(entity);
    }

    private ServiceDto toDto(ServiceEntity entity) {
        ImageDto imageDto = null;
        if (entity.getImageId() != null) {
            imageDto = imageRepository.findById(Long.valueOf(entity.getImageId()))
                    .map(img -> ImageDto.builder()
                            .id(img.getId())
                            .url("/api/v1/catalog/images/" + img.getId())
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
