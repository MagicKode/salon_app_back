package com.service.salon.catalog.model.dto;

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
public class CreateMasterRequest {

    @NotBlank(message = "Имя обязательно")
    private String name;

    @NotBlank(message = "Должность обязательна")
    private String position;

    private String description;

    @NotNull(message = "ID изображения обязательно")
    private Long imageId;
}
