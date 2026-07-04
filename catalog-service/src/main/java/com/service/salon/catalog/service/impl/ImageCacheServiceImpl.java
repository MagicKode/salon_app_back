package com.service.salon.catalog.service.impl;

import com.service.salon.catalog.model.ImageEntity;
import com.service.salon.catalog.model.dto.ImageDto;
import com.service.salon.catalog.repository.ImageRepository;
import com.service.salon.catalog.service.ImageCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageCacheServiceImpl implements ImageCacheService {
    private final ImageRepository imageRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "images", key = "#relatedType + '_' + #relatedId")
    public List<ImageDto> getImagesByRelated(String relatedType, Long relatedId) {
        return imageRepository.findByRelatedTypeAndRelatedIdOrderByCreatedAtDesc(relatedType, relatedId)
                .stream().map(this::toDto).toList();
    }

    private ImageDto toDto(ImageEntity entity) {
        return ImageDto.builder()
                .id(entity.getId())
                .url("/api/v1/catalog/images/" + entity.getId())
                .relatedType(entity.getRelatedType())
                .relatedId(entity.getRelatedId())
                .contentType(entity.getContentType())
                .originalName(entity.getOriginalName())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
