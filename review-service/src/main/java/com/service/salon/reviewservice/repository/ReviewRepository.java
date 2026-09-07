package com.service.salon.reviewservice.repository;

import com.service.salon.reviewservice.model.Review;
import com.service.salon.reviewservice.model.dto.ReviewBatchStatsDto;
import com.service.salon.reviewservice.model.dto.ReviewStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Постраничный вывод отзывов для бесконечной ленты
    Page<Review> findByMasterIdOrderByCreatedAtDesc(Long masterId, Pageable pageable);

    // Расчет полной гистограммы для одного мастера
    @Query("SELECT new com.service.salon.reviewservice.model.dto.ReviewStatsDto(" +
            "COALESCE(AVG(r.rating), 0.0), " + // Тут у тебя уже было, отлично
            "COUNT(r), " +                     // COUNT сам по себе возвращает 0, тут всё ок
            "COALESCE(SUM(CASE WHEN r.rating = 5 THEN 1 ELSE 0 END), 0L), " +
            "COALESCE(SUM(CASE WHEN r.rating = 4 THEN 1 ELSE 0 END), 0L), " +
            "COALESCE(SUM(CASE WHEN r.rating = 3 THEN 1 ELSE 0 END), 0L), " +
            "COALESCE(SUM(CASE WHEN r.rating = 2 THEN 1 ELSE 0 END), 0L), " +
            "COALESCE(SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END), 0L)) " +
            "FROM Review r WHERE r.masterId = :masterId")
    ReviewStatsDto getMasterStats(@Param("masterId") Long masterId);

    @Query("SELECT new com.service.salon.reviewservice.model.dto.ReviewBatchStatsDto(" +
            "r.masterId, COALESCE(AVG(r.rating), 0.0), COUNT(r)) " +
            "FROM Review r WHERE r.masterId IN :masterIds GROUP BY r.masterId")
    List<ReviewBatchStatsDto> getBatchStatsForMasters(@Param("masterIds") List<Long> masterIds);

}

