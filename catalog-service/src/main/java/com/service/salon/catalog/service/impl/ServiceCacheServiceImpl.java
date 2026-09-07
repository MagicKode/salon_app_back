package com.service.salon.catalog.service.impl;

import com.service.salon.catalog.mapper.ServiceMapper;
import com.service.salon.catalog.model.ServiceEntity;
import com.service.salon.catalog.model.dto.ImageDto;
import com.service.salon.catalog.model.dto.ServiceDto;
import com.service.salon.catalog.repository.ImageRepository;
import com.service.salon.catalog.repository.ServiceRepository;
import com.service.salon.catalog.service.ServiceCacheService;
import com.service.salon.commonservice.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCacheServiceImpl implements ServiceCacheService {

    private final ServiceRepository serviceRepository;
    private final ImageRepository imageRepository;
    private final ServiceMapper serviceMapper;

    @Override
    @Cacheable(value = "services", key = "'all_active'")
    public List<ServiceDto> getActiveServices() {
        return serviceRepository.findByIsActiveTrueOrderBySortOrderAsc()
                .stream().map(this::toDtoWithImage).toList();
    }

    @Override
    @Cacheable(value = "services", key = "'category_' + #categoryId")
    public List<ServiceDto> getServicesByCategory(Long categoryId) {
        return serviceRepository.findByCategoryIdAndIsActiveTrueOrderBySortOrderAsc(categoryId)
                .stream().map(this::toDtoWithImage).toList();
    }

    @Override
    @Transactional
    public ServiceEntity updateDescription(Long id, String newDescription) {
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Услуга не найдена"));

        entity.setDescription(newDescription);

        return serviceRepository.save(entity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "services", allEntries = true)
    public ServiceEntity softDeleteService(Long id) {
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Услуга не найдёна"));
        entity.setIsActive(false);
        return serviceRepository.save(entity);
    }

    private ServiceDto toDtoWithImage(ServiceEntity entity) {
        ServiceDto dto = serviceMapper.toDto(entity); // базовый маппинг

        // Добавляем изображение, если есть
        if (entity.getImageId() != null) {
            imageRepository.findById(Long.valueOf(entity.getImageId()))
                    .ifPresent(img -> {
                        ImageDto imageDto = ImageDto.builder()
                                .id(img.getId())
                                .url("/api/v1/catalog/images/" + img.getId())
                                .build();
                        dto.setImage(imageDto);
                    });
        }
        // isActive уже есть в DTO, т.к. маппер скопирует поле
        return dto;
    }
}
