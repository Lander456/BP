package com.example.bp.api.dto;

public record IrisFindingDetailDto(
        Long id,
        String irisImageImageId,
        IrisSectorDetailDto sector,
        ArtifactDetailDto artifact,
        String geometryJson,
        Double confidenceScore,
        Boolean isGenerated,
        Boolean isValidated
) {
}
