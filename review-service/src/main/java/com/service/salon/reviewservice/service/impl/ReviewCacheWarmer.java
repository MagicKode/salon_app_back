package com.service.salon.reviewservice.service.impl;

import com.service.salon.reviewservice.service.ReviewService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewCacheWarmer {

    private final ReviewService reviewService;

    @PostConstruct
    public void warmUpReviews() {
        try {
            // Статистика и отзывы для мастера с ID 1 (единственный мастер)
            reviewService.getMasterStats(1L);
            reviewService.getMasterReviews(1L, 0, 10);
            log.info("✅ Cache 'reviews' warmed up successfully");
        } catch (Exception e) {
            log.warn("⚠️ Could not warm up reviews cache: {}", e.getMessage());
        }
    }
}
