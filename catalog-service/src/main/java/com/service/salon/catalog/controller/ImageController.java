package com.service.salon.catalog.controller;

import com.service.salon.catalog.model.dto.ImageDto;
import com.service.salon.catalog.service.ImageCacheService;
import com.service.salon.catalog.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/catalog/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageCacheService imageCacheService;
    private final ImageService imageService;

    @CacheEvict(value = "images", key = "'gallery_0'")
    @PostMapping("/upload")
    public ResponseEntity<ImageDto> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("relatedType") String relatedType,
            @RequestParam(value = "relatedId", required = false) Long relatedId) throws IOException {
        return ResponseEntity.ok(imageService.uploadImage(file, relatedType, relatedId));
    }

    //Получить изображение для услуги/галереи
    @GetMapping("/by-related")
    public ResponseEntity<List<ImageDto>> getByRelatedType(
            @RequestParam("relatedType") String relatedType,
            @RequestParam("relatedId") Long relatedId,
            @RequestParam(required = false, defaultValue = "999") int limit) {

        final Long effectiveId = (relatedId == null) ? 0L : relatedId;
        List<ImageDto> allImages = imageCacheService.getImagesByRelated(relatedType, effectiveId);
        // Обрезаем до limit
        List<ImageDto> limitedImages = allImages.stream().limit(limit).toList();

        return ResponseEntity.ok(limitedImages);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        byte[] data = imageService.getImageData(id);
        String contentType = imageService.getContentType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "images", allEntries = true)
    @Transactional
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
