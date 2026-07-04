package com.service.salon.catalog.repository;

import com.service.salon.catalog.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity,Long> {
    List<ImageEntity> findByRelatedTypeAndRelatedIdOrderByCreatedAtDesc(String relatedType,Long relatedId);
}
