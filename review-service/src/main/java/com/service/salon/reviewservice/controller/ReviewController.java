package com.service.salon.reviewservice.controller;

import com.service.salon.reviewservice.model.dto.ReviewBatchStatsDto;
import com.service.salon.reviewservice.model.dto.ReviewCreateDto;
import com.service.salon.reviewservice.model.dto.ReviewResponseDto;
import com.service.salon.reviewservice.model.dto.ReviewStatsDto;
import com.service.salon.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewCreateDto reviewCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.save(reviewCreateDto));
    }

    @GetMapping("/masters/{masterId}/stats")
    public ResponseEntity<ReviewStatsDto> getReviews(@PathVariable Long masterId) {
        return ResponseEntity.ok(reviewService.getMasterStats(masterId));
    }

    @GetMapping("/masters/{masterId}")
    public ResponseEntity<List<ReviewResponseDto>> getMasterReviews(
            @PathVariable Long masterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(reviewService.getMasterReviews(masterId, page, size));
    }

    @PostMapping("/masters/stats/batch")
    public ResponseEntity<List<ReviewBatchStatsDto>> getMasterReviewsStats(@RequestParam List<Long> masterIds) {
        return ResponseEntity.ok(reviewService.getBatchStats(masterIds));
    }
}
