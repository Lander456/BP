package com.example.bp.api.mapper;

import com.example.bp.api.dto.IrisMapCreateDto;
import com.example.bp.api.dto.IrisMapDetailDto;
import com.example.bp.api.dto.IrisMapListDto;
import com.example.bp.api.dto.IrisMapUpdateDto;
import com.example.bp.dal.entity.IrisMap;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IrisMapMapper {
    IrisMap toEntity(IrisMapCreateDto dto);
    IrisMap toEntity(IrisMapUpdateDto dto);
    IrisMapDetailDto toDetailDto(IrisMap entity);
    List<IrisMapListDto> toListDtoList(List<IrisMap> entities);
}
