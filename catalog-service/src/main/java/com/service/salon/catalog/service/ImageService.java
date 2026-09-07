package com.service.salon.catalog.service;

import com.service.salon.catalog.model.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    ImageDto uploadImage(MultipartFile file, String relatedType, Long relatedId) throws IOException;
    byte[] getImageData(Long id);
    String getContentType(Long id);
    void deleteImage(Long id);
}
