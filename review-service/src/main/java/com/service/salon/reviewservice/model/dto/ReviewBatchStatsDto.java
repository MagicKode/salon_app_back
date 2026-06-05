package com.service.salon.reviewservice.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewBatchStatsDto {
    private Long masterId;
    private Double averageRating;
    private Long totalReviews;
}
