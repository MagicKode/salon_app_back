package com.service.salon.catalog.service;

import com.service.salon.catalog.model.dto.ImageDto;

import java.util.List;

public interface ImageCacheService {
    List<ImageDto> getImagesByRelated(String relatedType, Long relatedId);
}
