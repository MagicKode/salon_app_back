package com.service.salon.catalog.service;

import com.service.salon.catalog.model.dto.ServiceDto;

import java.util.List;

public interface ServiceCacheService {
    List<ServiceDto> getActiveServices();
    List<ServiceDto> getServicesByCategory(Long categoryId);
}
