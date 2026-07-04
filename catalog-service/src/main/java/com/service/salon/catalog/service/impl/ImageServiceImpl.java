package com.service.salon.catalog.service.impl;

import com.service.salon.catalog.model.ImageEntity;
import com.service.salon.catalog.model.dto.ImageDto;
import com.service.salon.catalog.repository.ImageRepository;
import com.service.salon.catalog.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public ImageDto uploadImage(MultipartFile file, String relatedType, Long relatedId) throws IOException {
        ImageEntity entity = ImageEntity.builder()
                .relatedType(relatedType)
                .relatedId(relatedId)
                .imageData(file.getBytes())
                .contentType(file.getContentType())
                .originalName(file.getOriginalFilename())
                .build();

        entity = imageRepository.save(entity);
        String url = "/api/v1/catalog/images/" + entity.getId();
        return ImageDto.builder()
                .id(entity.getId())
                .url(url)
                .relatedType(entity.getRelatedType())
                .relatedId(entity.getRelatedId())
                .contentType(entity.getContentType())
                .originalName(entity.getOriginalName())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getImageData(Long id) {
        ImageEntity entity = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return entity.getImageData();
    }

    @Override
    @Transactional(readOnly = true)
    public String getContentType(Long id) {
        ImageEntity entity = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return entity.getContentType();
    }
}
