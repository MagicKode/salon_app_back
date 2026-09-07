package com.service.salon.reviewservice.mapper;

import com.service.salon.reviewservice.model.Review;
import com.service.salon.reviewservice.model.dto.ReviewCreateDto;
import com.service.salon.reviewservice.model.dto.ReviewResponseDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public class ReviewMapper {
    public Review toEntity(ReviewCreateDto dto) {
        if (dto == null) {
            return null;
        }

        return Review.builder()
                .masterId(dto.getMasterId())
                .clientName(dto.getClientName())
                .rating(dto.getRating())
                .comment(dto.getText()) // Из 'text' фронтенда в 'comment' базы данных
                .build();
    }

    public ReviewResponseDto toResponseDto(Review review) {
        if (review == null) {
            return null;
        }

        return ReviewResponseDto.builder()
                .id(review.getId())
                .masterId(review.getMasterId())
                .clientName(review.getClientName())
                .rating(review.getRating())
                .text(review.getComment()) // Из 'comment' базы данных в 'text' для JSON
                .createdAt(review.getCreatedAt())
                .build();
    }
}
