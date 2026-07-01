package com.example.bp.api.dto;

import com.example.bp.common.Sex;

public record PatientDetailDto(
        Long id,
        String firstName,
        String lastName,
        Byte age,
        String birthNum,
        Sex sex
) { }
