package com.service.salon.catalog.service;

import com.service.salon.basedomains.model.Salon;
import com.service.salon.catalog.model.ServiceEntity;
import com.service.salon.catalog.model.dto.CreateServiceRequest;

public interface CatalogService {
    Salon getMainSalon();
    Salon updateMainSalon(Salon salonDomain);
    ServiceEntity createService(CreateServiceRequest request);
}
