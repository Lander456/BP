package com.example.bp.api.mapper;

import com.example.bp.api.dto.ArtifactCreateDto;
import com.example.bp.api.dto.ArtifactDetailDto;
import com.example.bp.api.dto.ArtifactListDto;
import com.example.bp.api.dto.ArtifactUpdateDto;
import com.example.bp.dal.entity.Artifact;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtifactMapper {
    Artifact toEntity(@Nonnull ArtifactCreateDto dto);
    Artifact toEntity(@Nonnull ArtifactUpdateDto dto);
    ArtifactDetailDto toDetailDto(@Nonnull Artifact entity);
    List<ArtifactListDto> toListDtoList(@Nonnull List<Artifact> entities);
}
