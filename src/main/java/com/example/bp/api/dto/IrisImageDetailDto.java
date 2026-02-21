package com.example.bp.api.dto;

public record IrisImageDetailDto(
        Long id,
        String imageUrl,
        String originalFileName,
        String contentType,
        long fileSize,
        String patientFirstName,
        String patientLastName,
        Byte patientAge,
        Boolean patientSex
) { }
