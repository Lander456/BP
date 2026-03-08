package com.example.bp.api.dto;

import jakarta.annotation.Nonnull;

import java.util.List;

public record IrisMapUpdateDto(
        @Nonnull Long id,
        String imageUrl,
        List<IrisSectorCreateDto> sectors
) { }
