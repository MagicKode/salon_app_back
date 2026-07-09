package com.service.salon.catalog.mapper;

import com.service.salon.catalog.model.ServiceEntity;
import com.service.salon.catalog.model.dto.ServiceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceDto toDto(ServiceEntity entity);
}
