package com.example.bp.service.impl;

import com.example.bp.api.dto.IrisSectorCreateDto;
import com.example.bp.api.dto.IrisSectorDetailDto;
import com.example.bp.api.dto.IrisSectorListDto;
import com.example.bp.api.dto.IrisSectorUpdateDto;
import com.example.bp.api.mapper.IrisSectorMapper;
import com.example.bp.dal.entity.IrisMap;
import com.example.bp.dal.entity.IrisSector;
import com.example.bp.dal.repository.IrisMapRepository;
import com.example.bp.dal.repository.IrisSectorRepository;
import com.example.bp.service.IrisSectorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IrisSectorServiceImpl implements IrisSectorService {

    private final IrisMapRepository irisMapRepository;
    private final IrisSectorRepository irisSectorRepository;
    private final IrisSectorMapper irisSectorMapper;

    @Override
    public IrisSectorDetailDto create(@NonNull IrisSectorCreateDto dto) {
        IrisSector irisSector = irisSectorMapper.toEntity(dto);

        IrisMap irisMap = irisMapRepository.getReferenceById(dto.irisMapId());
        irisSector.setIrisMap(irisMap);

        IrisSector saved = irisSectorRepository.save(irisSector);

        return irisSectorMapper.toDetailDto(saved);
    }

    @Override
    public IrisSectorDetailDto update(@NonNull IrisSectorUpdateDto dto) {
        IrisSector irisSector = irisSectorRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Failed to fetch irisMap from DB with ID: " + dto.id()));

        irisSectorMapper.updateEntityFromDto(dto, irisSector);

        IrisSector saved = irisSectorRepository.save(irisSector);

        return irisSectorMapper.toDetailDto(saved);
    }

    @Override
    public List<IrisSectorListDto> getByMapId(Long mapId) {
        List<IrisSector> irisSectors = irisSectorRepository.findByIrisMapId(mapId);

        return irisSectorMapper.toListDtoList(irisSectors);
    }

    @Override
    public IrisSectorDetailDto getByMapIdAndName(Long mapId, String name) {
        IrisSector irisSector = irisSectorRepository.findByIrisMapIdAndName(mapId, name)
                .orElseThrow(() -> new EntityNotFoundException("Failed to fetch irisSector from DB, belonging to map with ID: " + mapId + " and name: " + name));

        return irisSectorMapper.toDetailDto(irisSector);
    }

    @Override
    public void delete(Long id) {
        irisSectorRepository.deleteById(id);
    }
}
