package com.example.bp.service;

import com.example.bp.api.dto.PatientCreateDto;
import com.example.bp.api.dto.PatientDetailDto;
import com.example.bp.api.dto.PatientListDto;
import com.example.bp.api.dto.PatientUpdateDto;

import java.util.List;

public interface PatientService {
    PatientDetailDto create(PatientCreateDto dto);
    PatientDetailDto getById(Long id);
    List<PatientListDto> getAll();
    List<PatientListDto> getByFirstName(String firstName);
    List<PatientListDto> getByLastName(String lastName);
    void update(Long id, PatientUpdateDto dto);
    void delete(Long id);
}
