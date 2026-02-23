package com.example.bp.api.mapper;

import com.example.bp.api.dto.IridologistCreateDto;
import com.example.bp.api.dto.IridologistDetailDto;
import com.example.bp.api.dto.IridologistListDto;
import com.example.bp.api.dto.IridologistUpdateDto;
import com.example.bp.dal.entity.Iridologist;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IridologistMapper {
    Iridologist toEntity(@Nonnull IridologistCreateDto dto);
    Iridologist toEntity(@Nonnull IridologistUpdateDto dto);
    IridologistDetailDto toDetailDto(@Nonnull Iridologist entity);
    List<IridologistListDto> toListDtoList(List<Iridologist> entities);
}
