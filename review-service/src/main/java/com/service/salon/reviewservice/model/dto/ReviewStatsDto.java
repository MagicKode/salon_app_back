package com.service.salon.reviewservice.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewStatsDto {
    private Double averageRating;
    private Long totalReviews;
    private Long star5Count;
    private Long star4Count;
    private Long star3Count;
    private Long star2Count;
    private Long star1Count;

    public ReviewStatsDto(Double averageRating, Long totalReviews,
                          Long star5Count, Long star4Count, Long star3Count,
                          Long star2Count, Long star1Count) {
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.star5Count = star5Count;
        this.star4Count = star4Count;
        this.star3Count = star3Count;
        this.star2Count = star2Count;
        this.star1Count = star1Count;
    }
}
