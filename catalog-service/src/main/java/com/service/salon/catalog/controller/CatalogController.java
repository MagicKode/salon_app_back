package com.service.salon.catalog.controller;

import com.service.salon.basedomains.model.Salon;
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

    @GetMapping("/salon")
    public ResponseEntity<ApiResponse<Salon>> getSalonInfo() {
        Salon salon = catalogService.getMainSalon();
        return new ResponseEntity<>(ApiResponse.success(salon), HttpStatus.OK);
    }
}
