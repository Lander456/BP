package com.example.bp.service;

import com.example.bp.api.dto.ArtifactCreateDto;
import com.example.bp.api.dto.ArtifactDetailDto;
import com.example.bp.api.dto.ArtifactListDto;
import com.example.bp.api.dto.ArtifactUpdateDto;

import java.util.List;

public interface ArtifactService {
    ArtifactDetailDto getById(Long id);
    ArtifactDetailDto getByLabelCode(String labelCode);
    ArtifactDetailDto create(ArtifactCreateDto dto);
    ArtifactDetailDto update(ArtifactUpdateDto dto);
    List<ArtifactListDto> getByName(String name);
    List<ArtifactListDto> getAll();
    void delete(Long id);
}
