package com.example.bp.tests.service;

import com.example.bp.api.dto.*;
import com.example.bp.api.mapper.IrisMapMapper;
import com.example.bp.api.mapper.IrisSectorMapper;
import com.example.bp.dal.entity.Iridologist;
import com.example.bp.dal.entity.IrisMap;
import com.example.bp.dal.entity.IrisSector;
import com.example.bp.dal.repository.IridologistRepository;
import com.example.bp.dal.repository.IrisMapRepository;
import com.example.bp.service.FileStorageService;
import com.example.bp.service.impl.IrisMapServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IrisMapServiceTests {

    @InjectMocks
    IrisMapServiceImpl irisMapService;

    @Mock
    private IrisMapRepository irisMapRepository;

    @Mock
    private IridologistRepository iridologistRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private IrisMapMapper irisMapMapper;

    @Mock
    private IrisSectorMapper irisSectorMapper;

    @Test
    void findByValidMapIdExecutes() {
        Long id = 1L;
        IrisMap irisMap = new IrisMap("mapUrl");
        IrisSectorDetailDto sectorDto = new IrisSectorDetailDto(1L, "mockSectorName", 0.0, 120.0, 22.0, 44.0, "mockDescription", id);
        IrisMapDetailDto mapDto = new IrisMapDetailDto(1L, irisMap.getImageUrl(), 1L, List.of(sectorDto));

        when(irisMapRepository.findByIdWithSectors(id)).thenReturn(Optional.of(irisMap));
        when(irisMapMapper.toDetailDto(irisMap)).thenReturn(mapDto);

        irisMapService.getById(id);

        verify(irisMapRepository).findByIdWithSectors(id);
        verify(irisMapMapper).toDetailDto(irisMap);

        verifyNoMoreInteractions(irisMapRepository);
        verifyNoMoreInteractions(irisMapMapper);

        verifyNoInteractions(iridologistRepository);
        verifyNoInteractions(fileStorageService);
        verifyNoInteractions(irisSectorMapper);
    }

    @Test
    void findByInvalidMapIdThrows() {
        Long id = 1L;

        when(irisMapRepository.findByIdWithSectors(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> irisMapService.getById(id));

        assertTrue(exception.getMessage().contains("Failed to fetch IrisMap from DB with ID: " + id));

        verify(irisMapRepository).findByIdWithSectors(id);

        verifyNoMoreInteractions(irisMapRepository);

        verifyNoInteractions(irisMapMapper);
        verifyNoInteractions(iridologistRepository);
        verifyNoInteractions(fileStorageService);
        verifyNoInteractions(irisSectorMapper);
    }

    @Test
    void createWithExistingIridologistExecutes() {
        Long iridologistId = 1L;
        Long mapId = 2L;

        Iridologist iridologist = new Iridologist("firstName", "lastName", "username", "pwd");
        IrisSectorCreateDto sectorCreateDto = new IrisSectorCreateDto(mapId, "sectorName", 120.0, 140.0, 22.0, 44.0, "sectorDescription");
        List<IrisSectorCreateDto> sectorCreateDtoList = List.of(sectorCreateDto);
        IrisMapCreateDto mapCreateDto = new IrisMapCreateDto("mockImageUrl", iridologistId, sectorCreateDtoList);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(3L, sectorCreateDto.name(), sectorCreateDto.startAngle(), sectorCreateDto.endAngle(), sectorCreateDto.innerRadius(), sectorCreateDto.outerRadius(), sectorCreateDto.description(), sectorCreateDto.irisMapId());
        List<IrisSectorDetailDto> sectorDetailDtoList = List.of(sectorDetailDto);
        IrisMapDetailDto mapDetailDto = new IrisMapDetailDto(mapId, mapCreateDto.imageUrl(), mapCreateDto.iridologistId(), sectorDetailDtoList);
        IrisMap irisMap = new IrisMap("mapUrl");
        IrisSector irisSector = new IrisSector(sectorCreateDto.name(), sectorCreateDto.startAngle(), sectorCreateDto.endAngle());
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "mockFile.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "mockImage".getBytes()
        );

        when(fileStorageService.save(mockFile)).thenReturn("generatedUniqueName");
        when(iridologistRepository.findById(mapCreateDto.iridologistId())).thenReturn(Optional.of(iridologist));
        when(irisMapRepository.save(any(IrisMap.class))).thenReturn(irisMap);
        when(irisMapMapper.toDetailDto(irisMap)).thenReturn(mapDetailDto);
        when(irisSectorMapper.toEntity(sectorCreateDto)).thenReturn(irisSector);

        irisMapService.create(mapCreateDto, mockFile);

        verify(fileStorageService).save(mockFile);
        verify(iridologistRepository).findById(mapCreateDto.iridologistId());
        verify(irisSectorMapper).toEntity(sectorCreateDto);
        verify(irisMapRepository).save(any(IrisMap.class));
        verify(irisMapMapper).toDetailDto(irisMap);

        verifyNoMoreInteractions(fileStorageService);
        verifyNoMoreInteractions(iridologistRepository);
        verifyNoMoreInteractions(irisMapRepository);
        verifyNoMoreInteractions(irisMapMapper);
        verifyNoMoreInteractions(irisSectorMapper);
    }

    @Test
    void createWithNonExistingIridologistThrows() {
        Long iridologistId = 1L;
        Long mapId = 2L;

        Iridologist iridologist = new Iridologist("firstName", "lastName", "username", "pwd");
        IrisSectorCreateDto sectorCreateDto = new IrisSectorCreateDto(mapId, "sectorName", 120.0, 140.0, 22.0, 44.0, "sectorDescription");
        List<IrisSectorCreateDto> sectorCreateDtoList = List.of(sectorCreateDto);
        IrisMapCreateDto mapCreateDto = new IrisMapCreateDto("mockImageUrl", iridologistId, sectorCreateDtoList);
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "mockFile.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "mockImage".getBytes()
        );

        when(iridologistRepository.findById(iridologistId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> irisMapService.create(mapCreateDto, mockFile));
        assertTrue(exception.getMessage().contains("Failed to fetch Iridologist from DB with id: " + iridologistId));

        verify(iridologistRepository).findById(iridologistId);

        verifyNoMoreInteractions(iridologistRepository);

        verifyNoInteractions(fileStorageService);
        verifyNoInteractions(irisMapRepository);
        verifyNoInteractions(irisMapMapper);
        verifyNoInteractions(irisSectorMapper);
    }

    @Test
    void createWithoutSectorsExecutes() {
        Long iridologistId = 1L;
        Long mapId = 2L;

        Iridologist iridologist = new Iridologist("firstName", "lastName", "username", "pwd");
        IrisMapCreateDto mapCreateDto = new IrisMapCreateDto("mockImageUrl", iridologistId, null);
        IrisMapDetailDto mapDetailDto = new IrisMapDetailDto(mapId, mapCreateDto.imageUrl(), mapCreateDto.iridologistId(), null);
        IrisMap irisMap = new IrisMap("mapUrl");
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "mockFile.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "mockImage".getBytes()
        );

        when(fileStorageService.save(mockFile)).thenReturn("generatedUniqueName");
        when(iridologistRepository.findById(mapCreateDto.iridologistId())).thenReturn(Optional.of(iridologist));
        when(irisMapRepository.save(any(IrisMap.class))).thenReturn(irisMap);
        when(irisMapMapper.toDetailDto(irisMap)).thenReturn(mapDetailDto);

        irisMapService.create(mapCreateDto, mockFile);

        verify(fileStorageService).save(mockFile);
        verify(iridologistRepository).findById(mapCreateDto.iridologistId());
        verify(irisMapRepository).save(any(IrisMap.class));
        verify(irisMapMapper).toDetailDto(irisMap);

        verifyNoMoreInteractions(fileStorageService);
        verifyNoMoreInteractions(iridologistRepository);
        verifyNoMoreInteractions(irisMapRepository);
        verifyNoMoreInteractions(irisMapMapper);

        verifyNoInteractions(irisSectorMapper);
    }

    @Test
    void updateExistingIrisMapExecutes() {
        Long irisMapId = 1L;
        Long irisSectorId = 2L;
        Long iridologistId = 3L;

        IrisSectorCreateDto newSectorDto = new IrisSectorCreateDto(irisMapId, "newSector", 120.0, 140.0, 22.0, 44.0, "newDescription");
        List<IrisSectorCreateDto> newSectorDtoList = List.of(newSectorDto);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(irisSectorId, newSectorDto.name(), newSectorDto.startAngle(), newSectorDto.endAngle(), newSectorDto.innerRadius(), newSectorDto.outerRadius(), newSectorDto.description(), irisMapId);
        List<IrisSectorDetailDto> sectorDetailDtoList = List.of(sectorDetailDto);
        IrisMapUpdateDto updateDto = new IrisMapUpdateDto(1L, "newImageUrl", newSectorDtoList);
        IrisMapDetailDto irisMapDetailDto = new IrisMapDetailDto(irisMapId, "mapUrl", iridologistId, sectorDetailDtoList);
        IrisMap irisMap = new IrisMap(irisMapDetailDto.imageUrl());
        IrisSector irisSector = new IrisSector(newSectorDto.name(), newSectorDto.startAngle(), newSectorDto.endAngle());

        when(irisMapRepository.findById(updateDto.id())).thenReturn(Optional.of(irisMap));
        when(irisSectorMapper.toEntity(newSectorDto)).thenReturn(irisSector);
        when(irisMapMapper.toDetailDto(irisMap)).thenReturn(irisMapDetailDto);

        irisMapService.update(updateDto);

        verify(irisMapRepository).findById(irisMapId);
        verify(irisSectorMapper).toEntity(newSectorDto);
        verify(irisMapMapper).toDetailDto(irisMap);

        verifyNoMoreInteractions(irisMapRepository);
        verifyNoMoreInteractions(irisMapMapper);
        verifyNoMoreInteractions(irisSectorMapper);

        verifyNoInteractions(fileStorageService);
        verifyNoInteractions(iridologistRepository);
    }

    @Test
    void updateNonExistingIrisMapThrows() {
        Long irisMapId = 1L;

        IrisMapUpdateDto updateDto = new IrisMapUpdateDto(irisMapId, "newImageUrl", null);

        when(irisMapRepository.findById(irisMapId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> irisMapService.update(updateDto));
        assertTrue(exception.getMessage().contains("Failed to fetch IrisMap from DB with id: " + irisMapId));

        verify(irisMapRepository).findById(irisMapId);

        verifyNoMoreInteractions(irisMapRepository);

        verifyNoInteractions(fileStorageService);
        verifyNoInteractions(irisMapMapper);
        verifyNoInteractions(irisSectorMapper);
        verifyNoInteractions(iridologistRepository);
    }

    @Test
    void updateWithNoSectorsExecutes() {
        Long irisMapId = 1L;
        Long iridologistId = 2L;

        IrisMapUpdateDto updateDto = new IrisMapUpdateDto(1L, "newImageUrl", null);
        IrisMapDetailDto irisMapDetailDto = new IrisMapDetailDto(irisMapId, "mapUrl", iridologistId, null);
        IrisMap irisMap = new IrisMap(irisMapDetailDto.imageUrl());

        when(irisMapRepository.findById(irisMapId)).thenReturn(Optional.of(irisMap));
        when(irisMapMapper.toDetailDto(irisMap)).thenReturn(irisMapDetailDto);

        irisMapService.update(updateDto);

        verify(irisMapRepository).findById(irisMapId);
        verify(irisMapMapper).toDetailDto(irisMap);

        verifyNoMoreInteractions(irisMapRepository);
        verifyNoMoreInteractions(irisMapMapper);

        verifyNoInteractions(irisSectorMapper);
        verifyNoInteractions(iridologistRepository);
        verifyNoInteractions(fileStorageService);
    }

    @Test
    void updateWithNoImageUrlExecutes() {
        Long irisMapId = 1L;
        Long irisSectorId = 2L;
        Long iridologistId = 3L;

        IrisSectorCreateDto newSectorDto = new IrisSectorCreateDto(irisMapId, "newSector", 120.0, 140.0, 22.0, 44.0, "newDescription");
        List<IrisSectorCreateDto> newSectorDtoList = List.of(newSectorDto);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(irisSectorId, newSectorDto.name(), newSectorDto.startAngle(), newSectorDto.endAngle(), newSectorDto.innerRadius(), newSectorDto.outerRadius(), newSectorDto.description(), irisMapId);
        List<IrisSectorDetailDto> sectorDetailDtoList = List.of(sectorDetailDto);
        IrisMapUpdateDto updateDto = new IrisMapUpdateDto(1L, null, newSectorDtoList);
        IrisMapDetailDto irisMapDetailDto = new IrisMapDetailDto(irisMapId, "mapUrl", iridologistId, sectorDetailDtoList);
        IrisMap irisMap = new IrisMap(irisMapDetailDto.imageUrl());
        IrisSector irisSector = new IrisSector(newSectorDto.name(), newSectorDto.startAngle(), newSectorDto.endAngle());

        when(irisMapRepository.findById(irisMapId)).thenReturn(Optional.of(irisMap));
        when(irisSectorMapper.toEntity(newSectorDto)).thenReturn(irisSector);
        when(irisMapMapper.toDetailDto(irisMap)).thenReturn(irisMapDetailDto);

        irisMapService.update(updateDto);

        verify(irisMapRepository).findById(irisMapId);

        verify(irisSectorMapper).toEntity(newSectorDto);
        verify(irisMapMapper).toDetailDto(irisMap);

        verifyNoMoreInteractions(irisMapRepository);
        verifyNoMoreInteractions(irisMapMapper);
        verifyNoMoreInteractions(irisSectorMapper);

        verifyNoInteractions(iridologistRepository);
        verifyNoInteractions(fileStorageService);
    }

    @Test
    void getByIridologistIdExecutes() {
        Long iridologistId = 1L;
        Long irisMapId = 2L;

        IrisMap irisMap = new IrisMap("mapUrl");
        List<IrisMap> irisMapList = List.of(irisMap);
        IrisMapListDto irisMapListDto = new IrisMapListDto(irisMapId, irisMap.getImageUrl(), iridologistId, 0);
        List<IrisMapListDto> irisMapListDtoList = List.of(irisMapListDto);

        when(irisMapRepository.findByIridologistId(iridologistId)).thenReturn(irisMapList);
        when(irisMapMapper.toListDtoList(irisMapList)).thenReturn(irisMapListDtoList);

        irisMapService.getByIridologistId(iridologistId);

        verify(irisMapRepository).findByIridologistId(iridologistId);
        verify(irisMapMapper).toListDtoList(irisMapList);

        verifyNoMoreInteractions(irisMapRepository);
        verifyNoMoreInteractions(irisMapMapper);

        verifyNoInteractions(irisSectorMapper);
        verifyNoInteractions(iridologistRepository);
        verifyNoInteractions(fileStorageService);
    }
}
