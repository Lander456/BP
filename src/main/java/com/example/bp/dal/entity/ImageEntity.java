package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class ImageEntity{

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String imageUrl;

    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String storagePath;

    @EqualsAndHashCode.Include
    private String originalFileName;

    @EqualsAndHashCode.Include
    private String contentType;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;
}
