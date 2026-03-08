package com.example.bp.api.dto;

public record IrisFindingUpdateDto(
        Long irisSectorId,
        Long artifactId,
        String geometryJson,
        Boolean isValidated
) {
}
