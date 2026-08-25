package com.service.salon.catalog.controller;

import com.service.salon.catalog.mapper.ServiceMapper;
import com.service.salon.catalog.model.ServiceEntity;
import com.service.salon.catalog.model.dto.CreateServiceRequest;
import com.service.salon.catalog.model.dto.ServiceDto;
import com.service.salon.catalog.service.CatalogService;
import com.service.salon.catalog.service.ServiceCacheService;
import com.service.salon.commonservice.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/catalog/services")
@RequiredArgsConstructor
@Slf4j
public class ServiceController {

    private final ServiceCacheService cacheService;
    private final ServiceMapper serviceMapper;
    private final CatalogService catalogService;

    @GetMapping
    public ResponseEntity<List<ServiceDto>> getActiveServices() {
        return ResponseEntity.ok(cacheService.getActiveServices());
    }

    // Услуги конкретной категории
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ServiceDto>> getServicesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(cacheService.getServicesByCategory(categoryId));
    }

    @PatchMapping("/{id}")
    @CacheEvict(value = "services", allEntries = true)
    public ResponseEntity<ApiResponse<ServiceDto>> updateDescription(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String newDescription = request.get("description");
        if (newDescription == null || newDescription.trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("Поле 'description' обязательно"));
        }

        ServiceEntity updatedEntity = cacheService.updateDescription(id, newDescription);
        ServiceDto dto = serviceMapper.toDto(updatedEntity);

        return ResponseEntity.ok(
                ApiResponse.<ServiceDto>builder()
                        .success(true)
                        .message("Описание обновлено")
                        .data(dto)
                        .build()
        );
    }

    @PostMapping
    @CacheEvict(value = "services", allEntries = true)
    public ResponseEntity<ApiResponse<ServiceDto>> createService(
            @Valid @RequestBody CreateServiceRequest request) {
        log.info("Создание новой услуги: {}", request.getName());
        ServiceEntity entity = catalogService.createService(request);
        ServiceDto dto = serviceMapper.toDto(entity);
        return ResponseEntity.ok(
                ApiResponse.<ServiceDto>builder()
                        .success(true)
                        .message("Услуга успешно создана")
                        .data(dto)
                        .build()
        );
    }

    @PatchMapping("/{id}/deactivate")
    @CacheEvict(value = "services", allEntries = true)
    public ResponseEntity<ApiResponse<ServiceDto>> deactivateService(@PathVariable Long id) {
        ServiceEntity update = cacheService.softDeleteService(id);
        ServiceDto dto = serviceMapper.toDto(update);
        return ResponseEntity.ok(
                ApiResponse.<ServiceDto>builder()
                        .success(true)
                        .message("Услуга деактивирована")
                        .data(dto)
                        .build()
        );
    }
}
