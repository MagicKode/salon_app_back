package com.service.salon.reviewservice.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreateDto {

    @NotNull(message = "Master ID cannot be null")
    private Long masterId;

    @NotBlank(message = "Client name cannot be empty")
    private String clientName;

    @Min(1) @Max(5)
    private int rating;

    private String text;
}
