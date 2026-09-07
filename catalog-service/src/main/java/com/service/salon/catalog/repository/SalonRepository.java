package com.service.salon.catalog.repository;

import com.service.salon.catalog.model.SalonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonRepository extends JpaRepository<SalonEntity,Long> {
}
