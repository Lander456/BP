package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode
public abstract class ImageEntity{

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String imageUrl;

    @Column(unique = true, nullable = false)
    private String storagePath;

    private String originalFileName;

    private String contentType;

    private LocalDateTime uploadedAt;
}
