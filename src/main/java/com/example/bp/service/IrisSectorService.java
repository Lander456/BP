package com.example.bp.service;

import com.example.bp.api.dto.IrisSectorCreateDto;
import com.example.bp.api.dto.IrisSectorDetailDto;
import com.example.bp.api.dto.IrisSectorListDto;
import com.example.bp.api.dto.IrisSectorUpdateDto;
import com.example.bp.dal.entity.IrisSector;

import java.util.List;

public interface IrisSectorService {
    IrisSectorDetailDto create(IrisSectorCreateDto dto);
    IrisSectorDetailDto update(IrisSectorUpdateDto dto);
    List<IrisSectorListDto> getByMapId(Long mapId);
    IrisSectorDetailDto getByMapIdAndName(Long mapId, String name);
    void delete(Long id);
}
