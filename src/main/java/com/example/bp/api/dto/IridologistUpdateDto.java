package com.example.bp.api.dto;

import java.util.Optional;

public record IridologistUpdateDto(
        Optional<String> firstName,
        Optional<String> lastName,
        Optional<String> password
) { }
