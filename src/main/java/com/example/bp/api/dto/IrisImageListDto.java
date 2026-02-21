package com.example.bp.api.dto;
import java.time.LocalDateTime;

public record IrisImageListDto(
        Long id,
        String imageUrl,
        LocalDateTime uploadedAt,
        String label
) { }
