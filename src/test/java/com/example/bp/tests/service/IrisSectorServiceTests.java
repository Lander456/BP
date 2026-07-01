package com.example.bp.tests.service;

import com.example.bp.api.dto.IrisSectorCreateDto;
import com.example.bp.api.dto.IrisSectorDetailDto;
import com.example.bp.api.dto.IrisSectorUpdateDto;
import com.example.bp.api.mapper.IrisSectorMapper;
import com.example.bp.dal.entity.IrisMap;
import com.example.bp.dal.entity.IrisSector;
import com.example.bp.dal.repository.IrisMapRepository;
import com.example.bp.dal.repository.IrisSectorRepository;
import com.example.bp.service.impl.IrisSectorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class IrisSectorServiceTests {
    @InjectMocks
    IrisSectorServiceImpl irisSectorService;

    @Mock
    IrisMapRepository irisMapRepository;

    @Mock
    IrisSectorRepository irisSectorRepository;

    @Mock
    IrisSectorMapper irisSectorMapper;

    @Test
    void createIrisSectorExecutes() {
        Long irisMapId = 1L;
        Long irisSectorId = 2L;

        IrisMap irisMap = new IrisMap("mapUrl");
        IrisSectorCreateDto createDto = new IrisSectorCreateDto(irisMapId, "irisSectorName", 120.0, 140.0, 22.0, 44.0, "irisSectorDescription");
        IrisSector irisSector = new IrisSector(createDto.name(), createDto.startAngle(), createDto.endAngle());
        IrisSectorDetailDto detailDto = new IrisSectorDetailDto(irisSectorId, irisSector.getName(), irisSector.getStartAngle(), irisSector.getEndAngle(), irisSector.getInnerRadius(), irisSector.getOuterRadius(), irisSector.getDescription(), irisMapId);

        when(irisSectorMapper.toEntity(createDto)).thenReturn(irisSector);
        when(irisMapRepository.getReferenceById(irisMapId)).thenReturn(irisMap);
        when(irisSectorRepository.save(irisSector)).thenReturn(irisSector);
        when(irisSectorMapper.toDetailDto(irisSector)).thenReturn(detailDto);

        irisSectorService.create(createDto);

        verify(irisSectorMapper).toEntity(createDto);
        verify(irisMapRepository).getReferenceById(irisMapId);
        verify(irisSectorRepository).save(irisSector);
        verify(irisSectorMapper).toDetailDto(irisSector);

        verifyNoMoreInteractions(irisSectorMapper);
        verifyNoMoreInteractions(irisMapRepository);
        verifyNoMoreInteractions(irisSectorRepository);
    }

    @Test
    void updateExistingIrisSectorExecutes() {
        Long irisSectorId = 1L;

        IrisSectorUpdateDto updateDto = new IrisSectorUpdateDto(irisSectorId, "newIrisSectorName", 120.0, 140.0, 22.0, 44.0, "newIrisSectorDescription");
        IrisSector irisSector = new IrisSector(updateDto.name(), updateDto.startAngle(), updateDto.endAngle());
        IrisSectorDetailDto detailDto = new IrisSectorDetailDto(irisSectorId, irisSector.getName(), irisSector.getStartAngle(), irisSector.getEndAngle(), irisSector.getInnerRadius(), irisSector.getOuterRadius(), irisSector.getDescription(), 2L);

        when(irisSectorRepository.findById(irisSectorId)).thenReturn(Optional.of(irisSector));
        when(irisSectorRepository.save(irisSector)).thenReturn(irisSector);
        when(irisSectorMapper.toDetailDto(irisSector)).thenReturn(detailDto);

        irisSectorService.update(updateDto);

        verify(irisSectorRepository).findById(irisSectorId);
        verify(irisSectorMapper).updateEntityFromDto(updateDto, irisSector);
        verify(irisSectorRepository).save(irisSector);
        verify(irisSectorMapper).toDetailDto(irisSector);

        verifyNoMoreInteractions(irisSectorRepository);
        verifyNoMoreInteractions(irisSectorMapper);

        verifyNoInteractions(irisMapRepository);
    }

    @Test
    void updateNonExistingIrisSectorThrows() {
        Long irisSectorId = 1L;

        IrisSectorUpdateDto updateDto = new IrisSectorUpdateDto(irisSectorId, "newIrisSectorName", 120.0, 140.0, 22.0, 44.0, "newIrisSectorDescription");

        when(irisSectorRepository.findById(irisSectorId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> irisSectorService.update(updateDto));

        assertTrue(exception.getMessage().contains("Failed to fetch irisMap from DB with ID: " + irisSectorId));

        verify(irisSectorRepository).findById(irisSectorId);

        verifyNoMoreInteractions(irisSectorRepository);

        verifyNoInteractions(irisSectorMapper);
        verifyNoInteractions(irisMapRepository);
    }
}
