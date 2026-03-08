package com.example.bp.api.dto;

import java.util.List;

public record IrisMapDetailDto(
        Long id,
        String imageUrl,
        Long iridologistId,
        List<IrisSectorDetailDto> sectors
) { }
