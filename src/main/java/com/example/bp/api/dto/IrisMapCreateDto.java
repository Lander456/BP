package com.example.bp.api.dto;

import jakarta.annotation.Nonnull;

import java.util.List;

public record IrisMapCreateDto(
        @Nonnull String imageUrl,
        @Nonnull Long iridologistId,
        List<IrisSectorCreateDto> sectors
) { }
