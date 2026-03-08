package com.example.bp.service.impl;

import com.example.bp.api.dto.*;
import com.example.bp.api.mapper.IrisMapMapper;
import com.example.bp.api.mapper.IrisSectorMapper;
import com.example.bp.dal.entity.IrisMap;
import com.example.bp.dal.entity.IrisSector;
import com.example.bp.dal.repository.IridologistRepository;
import com.example.bp.dal.repository.IrisMapRepository;
import com.example.bp.service.FileStorageService;
import com.example.bp.service.IrisMapService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IrisMapServiceImpl implements IrisMapService {

    private final IrisMapRepository irisMapRepository;

    private final IridologistRepository iridologistRepository;

    private final FileStorageService fileStorageService;

    private final IrisMapMapper irisMapMapper;

    private final IrisSectorMapper irisSectorMapper;

    @Override
    public IrisMapDetailDto getById(Long id) {
        return irisMapRepository.findByIdWithSectors(id)
                .map(irisMapMapper::toDetailDto)
                .orElseThrow(() -> new EntityNotFoundException("Failed to fetch IrisMap from DB with ID: " + id));
    }

    @Override
    public IrisMapDetailDto create(@NonNull IrisMapCreateDto dto, MultipartFile file) {
        String fileName = fileStorageService.save(file);
        String path = "uploads/iris-maps/" + fileName;

        IrisMap irisMap = new IrisMap(dto.imageUrl());
        irisMap.setStoragePath(path);
        irisMap.setOriginalFileName(file.getOriginalFilename());
        irisMap.setContentType(file.getContentType());
        irisMap.setUploadedAt(LocalDateTime.now());

        irisMap.setIridologist(iridologistRepository.findById(dto.iridologistId()).
                orElseThrow(() -> new EntityNotFoundException("Failed to fetch Iridologist from DB with id: " + dto.iridologistId())));

        if (dto.sectors() != null) {
            dto.sectors().forEach(irisSectorDto -> {
                IrisSector sector = irisSectorMapper.toEntity(irisSectorDto);
                irisMap.addSector(sector);
            });
        }

        IrisMap saved = irisMapRepository.save(irisMap);
        return irisMapMapper.toDetailDto(saved);
    }

    @Override
    public IrisMapDetailDto update(@NonNull IrisMapUpdateDto dto) {
        IrisMap irisMap = irisMapRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Failed to fetch IrisMap from DB with id: " + dto.id()));

        if (dto.imageUrl() != null) {
            irisMap.setImageUrl(dto.imageUrl());
        }

        if (dto.sectors() != null) {
            dto.sectors().forEach(irisSectorDto -> {
                IrisSector sector = irisSectorMapper.toEntity(irisSectorDto);
                irisMap.addSector(sector);
            });
        }

        return irisMapMapper.toDetailDto(irisMap);
    }

    @Override
    public List<IrisMapListDto> getByIridologistId(Long iridologistId) {
        List<IrisMap> entities = irisMapRepository.findByIridologistId(iridologistId);

        return irisMapMapper.toListDtoList(entities);
    }

    @Override
    public List<IrisMapListDto> getAll() {
        List<IrisMap> entities = irisMapRepository.findAll();

        return irisMapMapper.toListDtoList(entities);
    }

    @Override
    public void delete(Long id) {
        IrisMap irisMap = irisMapRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to fetch irisMap from DB with id: " + id));

        irisMapRepository.delete(irisMap);
    }
}
