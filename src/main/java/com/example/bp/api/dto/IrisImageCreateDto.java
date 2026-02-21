package com.example.bp.api.dto;

import jakarta.annotation.Nonnull;

public record IrisImageCreateDto(
        @Nonnull Long patientId,
        String eyeSide,
        String notes
) { }
