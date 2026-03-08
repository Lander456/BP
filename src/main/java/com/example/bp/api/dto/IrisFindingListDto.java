package com.example.bp.api.dto;

public record IrisFindingListDto(
        Long id,
        String artifactName,
        String sectorName,
        Double confidenceScore,
        Boolean isGenerated,
        Boolean isValidated
) {
}
