package com.example.bp.api.dto;

import com.example.bp.common.Sex;

import java.util.Optional;

public record PatientUpdateDto(
        Optional<Long> iridologistId,
        Optional<String> firstName,
        Optional<String> lastName,
        Optional<Sex> sex,
        Optional<String> birthNum
) { }
