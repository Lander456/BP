package com.example.bp.api.mapper;

import com.example.bp.api.dto.IrisSectorCreateDto;
import com.example.bp.api.dto.IrisSectorDetailDto;
import com.example.bp.api.dto.IrisSectorListDto;
import com.example.bp.api.dto.IrisSectorUpdateDto;
import com.example.bp.dal.entity.IrisSector;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IrisSectorMapper {
    IrisSector toEntity(@Nonnull IrisSectorCreateDto dto);
    IrisSector toEntity(@Nonnull IrisSectorUpdateDto dto);
    IrisSectorDetailDto toDetailDto(@Nonnull IrisSector entity);
    List<IrisSectorListDto> toListDtoList(@Nonnull List<IrisSector> entities);
}
