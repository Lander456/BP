package com.example.bp.api.dto;

import jakarta.annotation.Nonnull;

public record IrisSectorCreateDto(
        @Nonnull Long irisMapId,
        @Nonnull String name,
        @Nonnull Double startAngle,
        @Nonnull Double endAngle,
        Double innerRadius,
        Double outerRadius,
        String description
) { }
