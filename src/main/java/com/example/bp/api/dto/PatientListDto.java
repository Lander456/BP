package com.example.bp.api.dto;

public record PatientListDto(
        Long id,
        Byte age,
        String firstName,
        String lastName
) { }
