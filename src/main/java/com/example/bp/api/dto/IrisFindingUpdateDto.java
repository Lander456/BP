package com.example.bp.api.dto;

import jakarta.annotation.Nonnull;

public record IrisFindingUpdateDto(
        @Nonnull Long id,
        Long irisSectorId,
        Long artifactId,
        String geometryJson,
        Boolean isValidated
) { }
