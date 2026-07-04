package com.service.salon.catalog.controller;

import com.service.salon.catalog.model.ImageEntity;
import com.service.salon.catalog.model.dto.ImageDto;
import com.service.salon.catalog.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    @PostMapping("/upload")
    public ResponseEntity<ImageDto> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("relatedType") String relatedType,
            @RequestParam(value = "relatedId", required = false) Long relatedId) throws IOException {

        ImageEntity entity = ImageEntity.builder()
                .relatedType(relatedType)
                .relatedId(relatedId)
                .imageData(file.getBytes())
                .contentType(file.getContentType())
                .originalName(file.getOriginalFilename())
                .build();

        entity = imageRepository.save(entity);
        String url = "/api/v1/catalog/images/" + entity.getId();
        return ResponseEntity.ok(
                ImageDto.builder()
                        .id(entity.getId())
                        .url(url)
                        .relatedType(entity.getRelatedType())
                        .relatedId(entity.getRelatedId())
                        .contentType(entity.getContentType())
                        .originalName(entity.getOriginalName())
                        .createdAt(entity.getCreatedAt())
                        .build()
        );
    }

    //Получить изображение для услуги/галереи
    @GetMapping("/by-related")
    @Transactional(readOnly = true)
    public ResponseEntity<List<ImageDto>> getByRelatedType(
            @RequestParam("relatedType") String relatedType,
            @RequestParam("relatedId") Long relatedId) {

        List<ImageDto> list = imageRepository.findByRelatedTypeAndRelatedIdOrderByCreatedAtDesc(relatedType, relatedId)
                .stream().map(e -> ImageDto.builder()
                        .id(e.getId())
                        .url("/api/v1/catalog/images/" + e.getId())
                        .relatedType(e.getRelatedType())
                        .relatedId(e.getRelatedId())
                        .contentType(e.getContentType())
                        .originalName(e.getOriginalName())
                        .createdAt(e.getCreatedAt())
                        .build())
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    // ← спасает от ошибки "Большие объекты не могут использоваться в режиме авто-подтверждения"
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        ImageEntity entity = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(entity.getContentType()));
        return new ResponseEntity<>(
                entity.getImageData(),
                headers,
                HttpStatus.OK
        );
    }
}
