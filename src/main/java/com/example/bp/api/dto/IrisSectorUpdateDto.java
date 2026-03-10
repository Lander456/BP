package com.example.bp.api.dto;

import jakarta.annotation.Nonnull;

public record IrisSectorUpdateDto(
        @Nonnull Long id,
        String name,
        Double startAngle,
        Double endAngle,
        Double innerRadius,
        Double outerRadius,
        String description
) {
}
