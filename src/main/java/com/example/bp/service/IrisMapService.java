package com.example.bp.service;

import com.example.bp.api.dto.*;
import com.example.bp.dal.entity.IrisMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IrisMapService {
    IrisMapDetailDto getById(Long id);
    IrisMapDetailDto create(IrisMapCreateDto dto, MultipartFile file);
    IrisMapDetailDto update(IrisMapUpdateDto dto);
    List<IrisMapListDto> getByIridologistId(Long iridologistId);
    List<IrisMapListDto> getAll();
    void delete(Long id);
}
