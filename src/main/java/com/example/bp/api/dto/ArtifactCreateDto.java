package com.example.bp.api.dto;

import jakarta.annotation.Nonnull;

public record ArtifactCreateDto(
        @Nonnull String name,
        String description,
        @Nonnull String labelCode
) {
}
