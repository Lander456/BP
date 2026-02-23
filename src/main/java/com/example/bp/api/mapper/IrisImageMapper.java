package com.example.bp.api.mapper;

import com.example.bp.api.dto.IrisImageCreateDto;
import com.example.bp.api.dto.IrisImageDetailDto;
import com.example.bp.api.dto.IrisImageListDto;
import com.example.bp.api.dto.IrisImageUpdateDto;
import com.example.bp.dal.entity.IrisImage;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IrisImageMapper {
    IrisImage toEntity(@Nonnull IrisImageCreateDto dto);
    IrisImage toEntity(@Nonnull IrisImageUpdateDto dto);
    IrisImageDetailDto toDetail(@Nonnull IrisImage entity);
    List<IrisImageListDto> toListDtoList(List<IrisImage> entities);
}
