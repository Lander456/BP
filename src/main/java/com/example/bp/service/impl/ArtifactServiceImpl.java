package com.example.bp.service.impl;

import com.example.bp.api.dto.ArtifactCreateDto;
import com.example.bp.api.dto.ArtifactDetailDto;
import com.example.bp.api.dto.ArtifactListDto;
import com.example.bp.api.dto.ArtifactUpdateDto;
import com.example.bp.api.mapper.ArtifactMapper;
import com.example.bp.dal.entity.Artifact;
import com.example.bp.dal.repository.ArtifactRepository;
import com.example.bp.service.ArtifactService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtifactServiceImpl implements ArtifactService {

    private final ArtifactRepository artifactRepository;

    private final ArtifactMapper mapper;

    @Override
    public ArtifactDetailDto getById(Long id) {
        return artifactRepository.findById(id)
                .map(mapper::toDetailDto)
                .orElseThrow(() -> new EntityNotFoundException("Artifact not found with ID: " + id));
    }

    @Override
    public ArtifactDetailDto getByLabelCode(String labelCode) {
        return artifactRepository.findByLabelCode(labelCode)
                .map(mapper::toDetailDto)
                .orElseThrow(() -> new EntityNotFoundException(("Artifact not found in DB with labelCode: " + labelCode)));
    }

    @Override
    public ArtifactDetailDto create(ArtifactCreateDto dto) {
        Artifact artifact = mapper.toEntity(dto);

        Artifact saved = artifactRepository.save(artifact);
        return mapper.toDetailDto(saved);
    }

    @Override
    public ArtifactDetailDto update(ArtifactUpdateDto dto) {
        Artifact entity = mapper.toEntity(dto);
        Artifact saved = artifactRepository.save(entity);

        return mapper.toDetailDto(saved);
    }

    @Override
    public List<ArtifactListDto> getByName(String name) {
        List<Artifact> entities = artifactRepository.findByName(name);
        return mapper.toListDtoList(entities);
    }

    @Override
    public List<ArtifactListDto> getAll() {
        List<Artifact> entities = artifactRepository.findAll();
        return mapper.toListDtoList(entities);
    }

    @Override
    public void delete(Long id) {
        artifactRepository.deleteById(id);
    }
}
