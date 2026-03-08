package com.example.bp.api.dto;

public record IrisSectorUpdateDto(
        String name,
        Double startAngle,
        Double endAngle,
        Double innerRadius,
        Double outerRadius,
        String description
) {
}
