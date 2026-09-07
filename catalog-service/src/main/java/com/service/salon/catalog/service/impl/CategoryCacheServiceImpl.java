package com.service.salon.catalog.service.impl;

import com.service.salon.catalog.model.CategoryEntity;
import com.service.salon.catalog.model.dto.CategoryDto;
import com.service.salon.catalog.model.dto.ImageDto;
import com.service.salon.catalog.repository.CategoryRepository;
import com.service.salon.catalog.repository.ImageRepository;
import com.service.salon.catalog.service.CategoryCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryCacheServiceImpl implements CategoryCacheService {

    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    @Override
    @Cacheable(value = "categories", key = "'all'")
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAllByOrderBySortOrderAsc()
                .stream().map(this::toDto).toList();
    }

    private CategoryDto toDto(CategoryEntity entity) {
        ImageDto imageDto = null;
        if (entity.getImageId() != null) {
            imageDto = imageRepository.findById(Long.valueOf(entity.getImageId()))
                    .map(img -> ImageDto.builder()
                            .id(img.getId())
                            .url("/api/v1/catalog/images/" + img.getId())
                            .build())
                    .orElse(null);
        }
        return CategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .image(imageDto)
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
