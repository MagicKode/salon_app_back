package com.service.salon.catalog.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "masters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String position; // должность/специализация

    @Column(columnDefinition = "TEXT")
    private String description; // опыт работы, достижения

    @Column(name = "image_id")
    private String imageId; // ссылка на фото в таблице images

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        if (isActive == null) isActive = true;
        if (sortOrder == null) sortOrder = 0;
    }
}
