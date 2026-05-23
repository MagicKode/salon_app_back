package com.service.salon.catalog.service.impl;

import com.service.salon.basedomains.model.Salon;
import com.service.salon.catalog.mapper.SalonMapper;
import com.service.salon.catalog.repository.SalonRepository;
import com.service.salon.catalog.service.CatalogService;
import com.service.salon.commonservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final SalonRepository repository;
    private final SalonMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Salon getMainSalon() {
        log.info("Fetching main salon details from database...");

        // Стучимся в репозиторий, ищем запись с ID 1, маппим её в Домен.
        // Если пусто — кидаем наш 404 эксепшн
        return repository.findById(1L)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Салон с ID 1 не найден в базе данных."));
    }
}
