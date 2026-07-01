package com.example.bp.api.dto;

import com.example.bp.common.Sex;

public record IrisImageDetailDto(
        Long id,
        String imageUrl,
        String originalFileName,
        String contentType,
        long fileSize,
        String patientFirstName,
        String patientLastName,
        Byte patientAge,
        Sex patientSex
) { }
