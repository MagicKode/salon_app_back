package com.service.salon.catalog.controller;

import com.service.salon.catalog.model.dto.CreateMasterRequest;
import com.service.salon.catalog.model.dto.MasterDto;
import com.service.salon.catalog.service.MasterService;
import com.service.salon.commonservice.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/masters")
@RequiredArgsConstructor
@Slf4j
public class MasterController {

    private final MasterService masterService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MasterDto>>> getActiveMasters() {
        List<MasterDto> masters = masterService.getActiveMasters();
        return ResponseEntity.ok(ApiResponse.success(masters));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MasterDto>> getMasterById(@PathVariable Long id) {
        MasterDto master = masterService.getMasterById(id);
        return ResponseEntity.ok(ApiResponse.success(master));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MasterDto>> createMaster(
            @Valid @RequestBody CreateMasterRequest request) {
        log.info("Creating master via API: {}", request.getName());
        MasterDto created = masterService.createMaster(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MasterDto>> updateMaster(
            @PathVariable Long id,
            @Valid @RequestBody CreateMasterRequest request) {
        log.info("Updating master via API: {}", id);
        MasterDto updated = masterService.updateMaster(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMaster(@PathVariable Long id) {
        log.info("Deleting master via API: {}", id);
        masterService.deleteMaster(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<MasterDto>> toggleActive(@PathVariable Long id) {
        log.info("Toggling master active status: {}", id);
        MasterDto updated = masterService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }
}
