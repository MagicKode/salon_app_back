package com.service.salon.catalog.mapper;

import com.service.salon.basedomains.model.Salon;
import com.service.salon.catalog.model.SalonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SalonMapper {
    SalonMapper INSTANCE = Mappers.getMapper(SalonMapper.class);

    // Конвертирует сущность из Postgres в чистый доменный объект для Flutter
    Salon toDomain(SalonEntity entity);

    // Конвертирует доменный объект обратно в сущность для сохранения в Postgres
    SalonEntity toEntity(Salon domain);
}
