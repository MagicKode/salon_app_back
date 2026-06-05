package com.service.salon.reviewservice.service.impl;

import com.service.salon.reviewservice.mapper.ReviewMapper;
import com.service.salon.reviewservice.model.Review;
import com.service.salon.reviewservice.model.dto.ReviewBatchStatsDto;
import com.service.salon.reviewservice.model.dto.ReviewCreateDto;
import com.service.salon.reviewservice.model.dto.ReviewResponseDto;
import com.service.salon.reviewservice.model.dto.ReviewStatsDto;
import com.service.salon.reviewservice.repository.ReviewRepository;
import com.service.salon.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;


    @Override
    public ReviewResponseDto save(ReviewCreateDto reviewCreateDto) {
        Review review = reviewMapper.toEntity(reviewCreateDto);
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toResponseDto(savedReview);
    }

    @Override
    public ReviewStatsDto getMasterStats(Long masterId) {
        return reviewRepository.getMasterStats(masterId);
    }

    @Override
    public List<ReviewBatchStatsDto> getBatchStats(List<Long> masterIds) {
        if (masterIds == null || masterIds.isEmpty()) {
            return List.of();
        }
        return reviewRepository.getBatchStatsForMasters(masterIds);
    }

    @Override
    public List<ReviewResponseDto> getMasterReviews(Long masterId, int page, int size) {
        Page<Review> reviewPage = reviewRepository.findByMasterIdOrderByCreatedAtDesc(
                masterId,
                PageRequest.of(page, size)
        );

        return reviewPage.getContent().stream()
                .map(reviewMapper::toResponseDto)
                .toList();
    }
}
