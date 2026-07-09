package com.service.salon.catalog.mapper;

import com.service.salon.basedomains.model.Salon;
import com.service.salon.catalog.model.SalonEntity;
import com.service.salon.catalog.model.dto.SalonDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalonMapper {
    // Из БД в Домен (для сервиса)
    Salon toDomain(SalonEntity entity);

    SalonDto toDto(Salon domain);

    SalonEntity toEntity(Salon domain);

    Salon toDomainFromDto(SalonDto dto);


}
