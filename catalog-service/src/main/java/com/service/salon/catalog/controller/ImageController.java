package com.service.salon.catalog.controller;

import com.service.salon.catalog.model.dto.ImageDto;
import com.service.salon.catalog.service.ImageCacheService;
import com.service.salon.catalog.service.ImageService;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/catalog/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageCacheService imageCacheService;
    private final ImageService imageService;

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
            @RequestParam("relatedId") Long relatedId) {

        final Long effectiveId = (relatedId == null) ? 0L : relatedId;
        return ResponseEntity.ok(imageCacheService.getImagesByRelated(relatedType, effectiveId));
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
}
