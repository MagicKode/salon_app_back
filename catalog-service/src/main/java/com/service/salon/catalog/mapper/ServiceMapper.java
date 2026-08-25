package com.service.salon.catalog.mapper;

import com.service.salon.catalog.model.ServiceEntity;
import com.service.salon.catalog.model.dto.ServiceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "image", ignore = true)
    ServiceDto toDto(ServiceEntity entity);
}
