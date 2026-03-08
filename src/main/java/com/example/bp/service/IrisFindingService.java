package com.example.bp.service;

import com.example.bp.api.dto.IrisFindingCreateDto;
import com.example.bp.api.dto.IrisFindingDetailDto;
import com.example.bp.api.dto.IrisFindingListDto;
import com.example.bp.api.dto.IrisFindingUpdateDto;

import java.util.List;

public interface IrisFindingService {
    IrisFindingDetailDto create(IrisFindingCreateDto dto);
    IrisFindingDetailDto getById(Long id);
    IrisFindingDetailDto update(IrisFindingUpdateDto dto);
    List<IrisFindingListDto> getAll();
    List<IrisFindingListDto> getByIrisImageId(Long irisImageId);
    List<IrisFindingListDto> getByArtifactLabelCode(String labelCode);
    List<IrisFindingListDto> getByIsValidated(Boolean isValidated);
    void delete(Long id);
}
