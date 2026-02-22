package com.example.bp.api.dto;

import java.util.Optional;

public record PatientUpdateDto(
        Optional<Long> iridologistId,
        Optional<String> firstName,
        Optional<String> lastName,
        Optional<Boolean> sex,
        Optional<String> birthNum
) { }
