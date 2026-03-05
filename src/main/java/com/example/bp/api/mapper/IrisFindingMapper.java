package com.example.bp.api.mapper;

import com.example.bp.api.dto.IrisFindingCreateDto;
import com.example.bp.api.dto.IrisFindingDetailDto;
import com.example.bp.api.dto.IrisFindingListDto;
import com.example.bp.api.dto.IrisFindingUpdateDto;
import com.example.bp.dal.entity.IrisFinding;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IrisFindingMapper {
    IrisFinding toEntity(@Nonnull IrisFindingCreateDto dto);
    IrisFinding toEntity(@Nonnull IrisFindingUpdateDto dto);
    IrisFindingDetailDto toDetailDto(@Nonnull IrisFinding entity);
    List<IrisFindingListDto> toListDtoList(@Nonnull List<IrisFinding> entities);
}
