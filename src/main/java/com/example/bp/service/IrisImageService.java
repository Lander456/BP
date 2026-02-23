package com.example.bp.service;

import com.example.bp.api.dto.IrisImageCreateDto;
import com.example.bp.api.dto.IrisImageDetailDto;
import com.example.bp.api.dto.IrisImageListDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IrisImageService {
    IrisImageDetailDto upload(MultipartFile file, IrisImageCreateDto metadata);
    IrisImageDetailDto getById(Long id);
    List<IrisImageListDto> getByPatientId(Long patientId);
    void delete(Long id);
}
