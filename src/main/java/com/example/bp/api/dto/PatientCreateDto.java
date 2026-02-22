package com.example.bp.api.dto;

public record PatientCreateDto(
        String firstName,
        String lastName,
        Byte age,
        String birthNum,
        Boolean sex
) { }
