package com.example.bp.api.mapper;

import com.example.bp.api.dto.PatientCreateDto;
import com.example.bp.api.dto.PatientDetailDto;
import com.example.bp.api.dto.PatientListDto;
import com.example.bp.api.dto.PatientUpdateDto;
import com.example.bp.dal.entity.Patient;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    public Patient toEntity(@Nonnull PatientUpdateDto dto);
    public Patient toEntity(@Nonnull PatientCreateDto dto);
    public PatientDetailDto toDetailDto(@Nonnull Patient entity);
    public List<PatientListDto> toListDtoList(List<Patient> entities);
}
