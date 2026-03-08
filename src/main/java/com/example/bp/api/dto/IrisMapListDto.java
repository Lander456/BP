package com.example.bp.api.dto;

public record IrisMapListDto(
        Long id,
        String imageUrl,
        Long iridologistId,
        int sectorCount
) { }
