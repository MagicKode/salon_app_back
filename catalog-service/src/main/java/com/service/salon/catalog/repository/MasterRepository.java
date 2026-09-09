package com.service.salon.catalog.repository;

import com.service.salon.catalog.model.MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterRepository extends JpaRepository<MasterEntity, Long> {
    List<MasterEntity> findByIsActiveTrueOrderBySortOrderAsc();
}
