package com.service.salon.catalog.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateServiceRequest {
    @NotBlank(message = "Название услуги обязательно")
    private String name;

    private String description;

    @NotNull(message = "Цена обязательна")
    @Positive(message = "Цена должна быть положительной")
    private Double price;

    @NotNull(message = "Длительность обязательна")
    @Min(value = 1, message = "Длительность должна быть минимум 1 минута")
    private Integer durationMinutes;

    private Integer categoryId;

    private Integer sortOrder;

    private String imageId;
}
