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
    Patient toEntity(@Nonnull PatientUpdateDto dto);
    Patient toEntity(@Nonnull PatientCreateDto dto);
    PatientDetailDto toDetailDto(@Nonnull Patient entity);
    List<PatientListDto> toListDtoList(List<Patient> entities);

    default <T> T mapOptional(java.util.Optional<T> optional) {
        return optional.isPresent() ? optional.orElse(null) : null;
    }
}
