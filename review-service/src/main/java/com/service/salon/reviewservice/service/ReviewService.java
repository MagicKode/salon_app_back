package com.service.salon.reviewservice.service;

import com.service.salon.reviewservice.model.Review;
import com.service.salon.reviewservice.model.dto.ReviewBatchStatsDto;
import com.service.salon.reviewservice.model.dto.ReviewCreateDto;
import com.service.salon.reviewservice.model.dto.ReviewResponseDto;
import com.service.salon.reviewservice.model.dto.ReviewStatsDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto save(ReviewCreateDto reviewCreateDto);
    ReviewStatsDto getMasterStats(Long masterId);
    List<ReviewBatchStatsDto> getBatchStats(List<Long> masterIds);
    List<ReviewResponseDto> getMasterReviews(Long masterId, int page, int size);
}
