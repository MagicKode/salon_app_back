package com.service.salon.catalog.controller;

import com.service.salon.catalog.model.dto.ServiceDto;
import com.service.salon.catalog.service.ServiceCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceCacheService cacheService;

    @GetMapping
    public ResponseEntity<List<ServiceDto>> getActiveServices(){
        return ResponseEntity.ok(cacheService.getActiveServices());
    }

    // Услуги конкретной категории
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ServiceDto>> getServicesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(cacheService.getServicesByCategory(categoryId));
    }
}
