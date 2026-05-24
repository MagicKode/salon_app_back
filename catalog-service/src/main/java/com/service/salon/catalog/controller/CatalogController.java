package com.service.salon.catalog.controller;

import com.service.salon.basedomains.model.Salon;
import com.service.salon.catalog.mapper.SalonMapper;
import com.service.salon.catalog.model.dto.SalonDto;
import com.service.salon.catalog.service.CatalogService;
import com.service.salon.commonservice.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;
    private final SalonMapper salonMapper;

    @GetMapping("/salon")
    public ResponseEntity<ApiResponse<SalonDto>> getSalonInfo() {
        Salon salonDomain = catalogService.getMainSalon();
        SalonDto salonDto = salonMapper.toDto(salonDomain);

        return new ResponseEntity<>(ApiResponse.success(salonDto), HttpStatus.OK);
    }
}
