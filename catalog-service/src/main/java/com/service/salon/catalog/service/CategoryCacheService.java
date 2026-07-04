package com.service.salon.catalog.service;

import com.service.salon.catalog.model.dto.CategoryDto;

import java.util.List;

public interface CategoryCacheService {
    List<CategoryDto> getAllCategories();
}
