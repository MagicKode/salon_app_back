package com.service.salon.catalog.service.impl;

import com.service.salon.basedomains.model.Salon;
import com.service.salon.catalog.mapper.SalonMapper;
import com.service.salon.catalog.model.SalonEntity;
import com.service.salon.catalog.repository.SalonRepository;
import com.service.salon.catalog.service.CatalogService;
import com.service.salon.commonservice.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final SalonRepository salonRepository;
    private final SalonMapper salonMapper;

    @Override
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
}
