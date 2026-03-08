package com.example.bp.api.dto;

import jakarta.annotation.Nonnull;

public record IrisFindingCreateDto(
        @Nonnull Long irisImageId,
        @Nonnull Long irisSectorId,
        @Nonnull Long artifactId,
        Double confidenceScore,
        @Nonnull String geometryJson
) {
}
