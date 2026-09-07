package com.service.salon.catalog.controller;

import com.service.salon.basedomains.model.Salon;
import com.service.salon.catalog.mapper.SalonMapper;
import com.service.salon.catalog.model.dto.SalonDto;
import com.service.salon.catalog.service.CatalogService;
import com.service.salon.commonservice.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
@Slf4j
public class CatalogController {

    private final CatalogService catalogService;
    private final SalonMapper salonMapper;

    @GetMapping("/salon")
    public ResponseEntity<ApiResponse<SalonDto>> getSalonInfo() {
        log.info("Получен запрос GET на получение информации о главном салоне");
        Salon salonDomain = catalogService.getMainSalon();
        SalonDto salonDto = salonMapper.toDto(salonDomain);

        return new ResponseEntity<>(ApiResponse.success(salonDto), HttpStatus.OK);
    }

    @PutMapping("/salon")
    public ResponseEntity<ApiResponse<SalonDto>> updateSalon(@Valid @RequestBody SalonDto salonDto) {
        // 1. Маппим пришедший DTO в доменную модель с помощью обновленного SalonMapper
        Salon salonDomain = salonMapper.toDomainFromDto(salonDto);

        // 2. Отправляем в сервис на обновление
        Salon updateDomain = catalogService.updateMainSalon(salonDomain);

        // 3. Возвращаем клиенту свежий DTO
        SalonDto responseGto = salonMapper.toDto(updateDomain);
        return new ResponseEntity<>(ApiResponse.success(responseGto), HttpStatus.OK);

    }
}
