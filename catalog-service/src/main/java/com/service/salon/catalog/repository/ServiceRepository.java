package com.service.salon.catalog.repository;

import com.service.salon.catalog.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity,Long> {
    List<ServiceEntity> findByIsActiveTrueOrderBySortOrderAsc();
    List<ServiceEntity> findByCategoryIdAndIsActiveTrueOrderBySortOrderAsc(Long categoryId);
}
