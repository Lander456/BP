package com.example.bp.service.impl;

import com.example.bp.api.dto.IrisFindingCreateDto;
import com.example.bp.api.dto.IrisFindingDetailDto;
import com.example.bp.api.dto.IrisFindingListDto;
import com.example.bp.api.dto.IrisFindingUpdateDto;
import com.example.bp.api.mapper.IrisFindingMapper;
import com.example.bp.dal.entity.Artifact;
import com.example.bp.dal.entity.IrisFinding;
import com.example.bp.dal.entity.IrisImage;
import com.example.bp.dal.entity.IrisSector;
import com.example.bp.dal.repository.ArtifactRepository;
import com.example.bp.dal.repository.IrisFindingRepository;
import com.example.bp.dal.repository.IrisImageRepository;
import com.example.bp.dal.repository.IrisSectorRepository;
import com.example.bp.service.IrisFindingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class IrisFindingServiceImpl implements IrisFindingService {

    private final IrisFindingRepository irisFindingRepository;
    private final IrisSectorRepository irisSectorRepository;
    private final IrisImageRepository irisImageRepository;
    private final ArtifactRepository artifactRepository;
    private final IrisFindingMapper mapper;

    @Override
    public IrisFindingDetailDto create(@NonNull IrisFindingCreateDto dto) {
        IrisImage irisImage = irisImageRepository.getReferenceById(dto.irisImageId());
        IrisSector irisSector = irisSectorRepository.getReferenceById(dto.irisSectorId());
        Artifact artifact = artifactRepository.getReferenceById(dto.artifactId());

        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, dto.geometryJson());

        if (dto.confidenceScore() != null) {
            irisFinding.setConfidenceScore(dto.confidenceScore());
        }

        IrisFinding saved = irisFindingRepository.save(irisFinding);
        return mapper.toDetailDto(saved);
    }

    @Override
    public IrisFindingDetailDto getById(Long id) {
        return irisFindingRepository.findById(id)
                .map(mapper::toDetailDto)
                .orElseThrow(() -> new EntityNotFoundException("Failed to fetch irisFinding from DB with id: " + id));
    }

    @Override
    public IrisFindingDetailDto update(@NonNull IrisFindingUpdateDto dto) {
        IrisFinding irisFinding = irisFindingRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Failed to fetch irisFinding from DB with id: " + dto.id()));

        if (dto.irisSectorId() != null) {
            IrisSector irisSector = irisSectorRepository.getReferenceById(dto.irisSectorId());

            irisFinding.setIrisSector(irisSector);
        }

        if (dto.artifactId() != null) {
            Artifact artifact = artifactRepository.getReferenceById(dto.artifactId());

            irisFinding.setArtifact(artifact);
        }

        if (dto.geometryJson() != null) {
            irisFinding.setGeometryJson(dto.geometryJson());
        }

        if (dto.isValidated() != null) {
            irisFinding.setIsValidated(dto.isValidated());
        }

        return mapper.toDetailDto(irisFinding);
    }

    @Override
    public List<IrisFindingListDto> getAll() {
        List<IrisFinding> entities = irisFindingRepository.findAll();

        return mapper.toListDtoList(entities);
    }

    @Override
    public List<IrisFindingListDto> getByIrisImageId(Long irisImageId) {
        List<IrisFinding> entities = irisFindingRepository.findByIrisImage_Id(irisImageId);

        return mapper.toListDtoList(entities);
    }

    @Override
    public List<IrisFindingListDto> getByArtifactLabelCode(String labelCode) {
        List<IrisFinding> entities = irisFindingRepository.findByArtifact_LabelCode(labelCode);

        return mapper.toListDtoList(entities);
    }

    @Override
    public List<IrisFindingListDto> getByIsValidated(Boolean isValidated) {
        List<IrisFinding> entities = irisFindingRepository.findByIsValidated(isValidated);

        return mapper.toListDtoList(entities);
    }

    @Override
    public void delete(Long id) {
        irisFindingRepository.deleteById(id);
    }
}
