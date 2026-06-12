package com.service.salon.booking.model.dto;

import lombok.Data;

@Data
public class NotificationRequestDto {
    private String title;
    private String body;
    private String type;
}
