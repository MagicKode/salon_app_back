package com.service.salon.catalog.mapper;

import com.service.salon.catalog.model.MasterEntity;
import com.service.salon.catalog.model.dto.ImageDto;
import com.service.salon.catalog.model.dto.MasterDto;
import com.service.salon.catalog.service.ImageCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MasterMapper {

    private final ImageCacheService imageCacheService;

    public MasterDto toDto(MasterEntity entity) {
        if (entity == null) return null;

        // Получаем фото, если есть imageId
        ImageDto image = null;
        if (entity.getImageId() != null) {
            try {
                Long imageId = Long.parseLong(entity.getImageId());
                List<ImageDto> images = imageCacheService.getImagesByRelated("master", imageId);
                if (!images.isEmpty()) {
                    image = images.get(0);
                }
            } catch (NumberFormatException e) {
                // игнорируем
            }
        }

        return MasterDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .position(entity.getPosition())
                .description(entity.getDescription())
                .image(image)
                .isActive(entity.getIsActive())
                .build();
    }
}
