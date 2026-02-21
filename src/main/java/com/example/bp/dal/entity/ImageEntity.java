package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode
public abstract class ImageEntity{

    @Id
    @GeneratedValue
    private Long Id;

    @Column(unique = true, nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String storagePath;

    private String originalFileName;

    private String contentType;

    private LocalDateTime uploadedAt;
}
