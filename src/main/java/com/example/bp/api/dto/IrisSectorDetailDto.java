package com.example.bp.api.dto;

public record IrisSectorDetailDto(
        Long id,
        String name,
        Double startAngle,
        Double endAngle,
        Double innerRadius,
        Double outerRadius,
        String description,
        Long irisMapId
) { }
