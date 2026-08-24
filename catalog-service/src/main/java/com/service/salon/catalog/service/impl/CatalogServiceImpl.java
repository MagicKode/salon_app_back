package com.service.salon.catalog.service.impl;

import com.service.salon.basedomains.model.Salon;
import com.service.salon.catalog.mapper.SalonMapper;
import com.service.salon.catalog.model.CategoryEntity;
import com.service.salon.catalog.model.SalonEntity;
import com.service.salon.catalog.model.ServiceEntity;
import com.service.salon.catalog.model.dto.CreateServiceRequest;
import com.service.salon.catalog.repository.CategoryRepository;
import com.service.salon.catalog.repository.SalonRepository;
import com.service.salon.catalog.repository.ServiceRepository;
import com.service.salon.catalog.service.CatalogService;
import com.service.salon.commonservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final SalonRepository salonRepository;
    private final SalonMapper salonMapper;
    private final ServiceRepository serviceRepository;
    private final CategoryRepository categoryRepository;

    @Override
//    @Cacheable(value = "salon", key = "'single'")
    @Transactional(readOnly = true)
    public Salon getMainSalon() {
        log.info("Fetching main salon details from database...");

        // Ищем салон с ID 1. Если не нашли — кидаем стандартный JPA Exception
        SalonEntity entity = salonRepository.findById(1L)
                .orElseThrow(() -> {
                    log.error("Main salon with ID 1 not found in database!");
                    return new ResourceNotFoundException("Main salon not found with id: 1");
                });

        return salonMapper.toDomain(entity);
    }

    @Override
    @CacheEvict(value = "salon", key = "'single'")
    @Transactional
    public Salon updateMainSalon(Salon salonDomain) {
        log.info("Обновление данных главного салона в базе данных для ID: 1");

        // 1. Извлекаем текущую сущность из БД. Если её нет — кидаем твою кастомную ошибку
        com.service.salon.catalog.model.SalonEntity entity = salonRepository.findById(1L)
                .orElseThrow(() -> new com.service.salon.commonservice.exception.ResourceNotFoundException("Main salon not found with id: 1"));

        // 2. Обновляем поля сущности (кроме ID и рейтинга, так как рейтинг считается по отзывам)
        entity.setName(salonDomain.getName());
        entity.setAddress(salonDomain.getAddress());
        entity.setDescription(salonDomain.getDescription());
        entity.setLatitude(salonDomain.getLatitude());
        entity.setLongitude(salonDomain.getLongitude());
        entity.setPhoneNumber(String.valueOf(salonDomain.getPhoneNumber()));
        entity.setWorkingHours(salonDomain.getWorkingHours());

        // 3. Сохраняем сущность обратно в PostgreSQL
        SalonEntity updateEntity = salonRepository.save(entity);

        // 4. Маппим обновленную сущность обратно в чистый бизнес-домен
        return salonMapper.toDomain(updateEntity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "services", allEntries = true)
    public ServiceEntity createService(CreateServiceRequest request) {
        log.info("Создание услуги: {}", request.getName());

        // Получаем категорию, если указана
        CategoryEntity category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(Long.valueOf(request.getCategoryId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена: " + request.getCategoryId()));
        }

        ServiceEntity entity = ServiceEntity.builder()
                .name(request.getName())
                .description(request.getDescription() != null ? request.getDescription() : "")
                .price(BigDecimal.valueOf(request.getPrice()))
                .durationMinutes(request.getDurationMinutes())
                .imageId(request.getImageId())
                .category(category)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .isActive(true)
                .build();

        return serviceRepository.save(entity);
    }
}
