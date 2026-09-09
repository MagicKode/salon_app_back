package com.service.salon.catalog.service.impl;

import com.service.salon.catalog.mapper.MasterMapper;
import com.service.salon.catalog.model.MasterEntity;
import com.service.salon.catalog.model.dto.CreateMasterRequest;
import com.service.salon.catalog.model.dto.MasterDto;
import com.service.salon.catalog.repository.MasterRepository;
import com.service.salon.catalog.service.MasterService;
import com.service.salon.commonservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;
    private final MasterMapper masterMapper;

    @Override
    @Cacheable(value = "masters", key = "'all'")
    @Transactional(readOnly = true)
    public List<MasterDto> getActiveMasters() {
        log.info("Fetching active masters from database");
        return masterRepository.findByIsActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(masterMapper::toDto)
                .toList();
    }

    @Override
    @Cacheable(value = "masters", key = "#id")
    @Transactional(readOnly = true)
    public MasterDto getMasterById(Long id) {
        log.info("Fetching master by id: {}", id);
        MasterEntity entity = masterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Master not found with id: " + id));
        return masterMapper.toDto(entity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "masters", allEntries = true)
    public MasterDto createMaster(CreateMasterRequest request) {
        log.info("Creating new master: {}", request.getName());

        MasterEntity entity = MasterEntity.builder()
                .name(request.getName())
                .position(request.getPosition())
                .description(request.getDescription())
                .imageId(String.valueOf(request.getImageId()))
                .isActive(true)
                .build();

        MasterEntity saved = masterRepository.save(entity);
        log.info("Master created with id: {}", saved.getId());
        return masterMapper.toDto(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "masters", allEntries = true)
    public MasterDto updateMaster(Long id, CreateMasterRequest request) {
        log.info("Updating master with id: {}", id);

        MasterEntity entity = masterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Master not found with id: " + id));

        entity.setName(request.getName());
        entity.setPosition(request.getPosition());
        entity.setDescription(request.getDescription());
        entity.setImageId(String.valueOf(request.getImageId()));

        MasterEntity updated = masterRepository.save(entity);
        return masterMapper.toDto(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = "masters", allEntries = true)
    public void deleteMaster(Long id) {
        log.info("Deleting master with id: {}", id);
        masterRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "masters", allEntries = true)
    public MasterDto toggleActive(Long id) {
        log.info("Toggling active status for master: {}", id);
        MasterEntity entity = masterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Master not found with id: " + id));
        entity.setIsActive(!entity.getIsActive());
        MasterEntity updated = masterRepository.save(entity);
        return masterMapper.toDto(updated);
    }
}
