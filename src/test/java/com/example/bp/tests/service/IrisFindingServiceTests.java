package com.example.bp.tests.service;

import com.example.bp.api.dto.*;
import com.example.bp.api.mapper.IrisFindingMapperImpl;
import com.example.bp.dal.entity.Artifact;
import com.example.bp.dal.entity.IrisFinding;
import com.example.bp.dal.entity.IrisImage;
import com.example.bp.dal.entity.IrisSector;
import com.example.bp.dal.repository.*;
import com.example.bp.service.impl.IrisFindingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IrisFindingServiceTests {

    @InjectMocks
    private IrisFindingServiceImpl irisFindingService;

    @Mock
    private IrisFindingMapperImpl irisFindingMapper;

    @Mock
    private IrisImageRepository irisImageRepository;

    @Mock
    private IrisSectorRepository irisSectorRepository;

    @Mock
    private ArtifactRepository artifactRepository;

    @Mock
    private IrisFindingRepository irisFindingRepository;

    @Test
    void createNewIrisFindingWithConfidenceScoreSucceeds() {
        IrisFindingCreateDto createDto = new IrisFindingCreateDto(1L, 1L, 1L, 68.0, "geometryJson");
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(1L, irisSector.getName(), irisSector.getStartAngle(), irisSector.getEndAngle(), irisSector.getInnerRadius(), irisSector.getOuterRadius(), irisSector.getDescription(), 1L);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        ArtifactDetailDto artifactDetailDto = new ArtifactDetailDto(1L, artifact.getName(), artifact.getDescription());
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, createDto.geometryJson());
        irisFinding.setConfidenceScore(createDto.confidenceScore());
        IrisFindingDetailDto detailDto = new IrisFindingDetailDto(1L, "imageId", sectorDetailDto, artifactDetailDto, irisFinding.getGeometryJson(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated());

        when(irisImageRepository.getReferenceById(createDto.irisImageId())).thenReturn(irisImage);
        when(irisSectorRepository.getReferenceById(createDto.irisSectorId())).thenReturn(irisSector);
        when(artifactRepository.getReferenceById(createDto.artifactId())).thenReturn(artifact);
        when(irisFindingRepository.save(any(IrisFinding.class))).thenReturn(irisFinding);
        when(irisFindingMapper.toDetailDto(irisFinding)).thenReturn(detailDto);

        irisFindingService.create(createDto);

        ArgumentCaptor<IrisFinding> captor = ArgumentCaptor.forClass(IrisFinding.class);

        verify(irisImageRepository).getReferenceById(createDto.irisImageId());
        verify(irisSectorRepository).getReferenceById(createDto.irisSectorId());
        verify(artifactRepository).getReferenceById(createDto.artifactId());
        verify(irisFindingRepository).save(captor.capture());
        verify(irisFindingMapper).toDetailDto(irisFinding);

        verifyNoMoreInteractions(irisImageRepository);
        verifyNoMoreInteractions(irisSectorRepository);
        verifyNoMoreInteractions(artifactRepository);
        verifyNoMoreInteractions(irisFindingRepository);
        verifyNoMoreInteractions(irisFindingMapper);

        IrisFinding capturedIrisFinding = captor.getValue();

        assertEquals(irisFinding.getConfidenceScore(), capturedIrisFinding.getConfidenceScore());
    }

    @Test
    void createNewIrisFindingWithoutConfidenceScoreSucceeds() {
        IrisFindingCreateDto createDto = new IrisFindingCreateDto(1L, 1L, 1L, null, "geometryJson");
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(1L, irisSector.getName(), irisSector.getStartAngle(), irisSector.getEndAngle(), irisSector.getInnerRadius(), irisSector.getOuterRadius(), irisSector.getDescription(), 1L);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        ArtifactDetailDto artifactDetailDto = new ArtifactDetailDto(1L, artifact.getName(), artifact.getDescription());
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, createDto.geometryJson());
        IrisFindingDetailDto detailDto = new IrisFindingDetailDto(1L, "imageId", sectorDetailDto, artifactDetailDto, irisFinding.getGeometryJson(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated());

        when(irisImageRepository.getReferenceById(createDto.irisImageId())).thenReturn(irisImage);
        when(irisSectorRepository.getReferenceById(createDto.irisSectorId())).thenReturn(irisSector);
        when(artifactRepository.getReferenceById(createDto.artifactId())).thenReturn(artifact);
        when(irisFindingRepository.save(any(IrisFinding.class))).thenReturn(irisFinding);
        when(irisFindingMapper.toDetailDto(irisFinding)).thenReturn(detailDto);

        irisFindingService.create(createDto);

        ArgumentCaptor<IrisFinding> captor = ArgumentCaptor.forClass(IrisFinding.class);

        verify(irisImageRepository).getReferenceById(createDto.irisImageId());
        verify(irisSectorRepository).getReferenceById(createDto.irisSectorId());
        verify(artifactRepository).getReferenceById(createDto.artifactId());
        verify(irisFindingRepository).save(captor.capture());
        verify(irisFindingMapper).toDetailDto(irisFinding);

        verifyNoMoreInteractions(irisImageRepository);
        verifyNoMoreInteractions(irisSectorRepository);
        verifyNoMoreInteractions(artifactRepository);
        verifyNoMoreInteractions(irisFindingRepository);
        verifyNoMoreInteractions(irisFindingMapper);

        IrisFinding capturedIrisFinding = captor.getValue();

        assertNull(capturedIrisFinding.getConfidenceScore());
    }

    @Test
    void getByExistingIdExecutes() {
        Long id = 1L;
        IrisFindingCreateDto createDto = new IrisFindingCreateDto(1L, 1L, 1L, 68.0, "geometryJson");
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(1L, irisSector.getName(), irisSector.getStartAngle(), irisSector.getEndAngle(), irisSector.getInnerRadius(), irisSector.getOuterRadius(), irisSector.getDescription(), 1L);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        ArtifactDetailDto artifactDetailDto = new ArtifactDetailDto(1L, artifact.getName(), artifact.getDescription());
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");
        irisFinding.setConfidenceScore(createDto.confidenceScore());
        IrisFindingDetailDto detailDto = new IrisFindingDetailDto(1L, "imageId", sectorDetailDto, artifactDetailDto, irisFinding.getGeometryJson(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated());

        when(irisFindingRepository.findById(id)).thenReturn(Optional.of(irisFinding));
        when(irisFindingMapper.toDetailDto(irisFinding)).thenReturn(detailDto);

        irisFindingService.getById(id);

        verify(irisFindingRepository).findById(id);
        verify(irisFindingMapper).toDetailDto(irisFinding);

        verifyNoMoreInteractions(irisImageRepository);
        verifyNoMoreInteractions(irisFindingMapper);
        verifyNoInteractions(artifactRepository);
        verifyNoInteractions(irisSectorRepository);
        verifyNoInteractions(irisImageRepository);
    }

    @Test
    void getByNonExistingIdThrows() {
        Long id = 1L;

        when(irisFindingRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> irisFindingService.getById(id));

        assertTrue(exception.getMessage().contains("Failed to fetch irisFinding from DB with id: " + id));
    }

    @Test
    void updateEverythingExecutes() {
        IrisFindingUpdateDto dto = new IrisFindingUpdateDto(1L, 1L, 1L, "newGeometryJson", true);
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(1L, irisSector.getName(), irisSector.getStartAngle(), irisSector.getEndAngle(), irisSector.getInnerRadius(), irisSector.getOuterRadius(), irisSector.getDescription(), 1L);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        ArtifactDetailDto artifactDetailDto = new ArtifactDetailDto(1L, artifact.getName(), artifact.getDescription());
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");
        IrisFindingDetailDto detailDto = new IrisFindingDetailDto(1L, "imageId", sectorDetailDto, artifactDetailDto, irisFinding.getGeometryJson(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated());

        when(irisFindingRepository.findById(dto.id())).thenReturn(Optional.of(irisFinding));
        when(irisFindingMapper.toDetailDto(irisFinding)).thenReturn(detailDto);
        when(artifactRepository.getReferenceById(dto.artifactId())).thenReturn(artifact);
        when(irisSectorRepository.getReferenceById(dto.irisSectorId())).thenReturn(irisSector);

        irisFindingService.update(dto);

        verify(irisFindingRepository).findById(dto.id());
        verify(irisFindingMapper).toDetailDto(irisFinding);
        verify(artifactRepository).getReferenceById(dto.artifactId());
        verify(irisSectorRepository).getReferenceById(dto.irisSectorId());

        verifyNoMoreInteractions(irisFindingMapper);
        verifyNoMoreInteractions(irisSectorRepository);
        verifyNoMoreInteractions(artifactRepository);
        verifyNoMoreInteractions(irisFindingRepository);

        verifyNoInteractions(irisImageRepository);
    }

    @Test
    void updateOnlyIrisSectorExecutes() {
        IrisFindingUpdateDto dto = new IrisFindingUpdateDto(1L, 1L, null, null, null);
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(1L, irisSector.getName(), irisSector.getStartAngle(), irisSector.getEndAngle(), irisSector.getInnerRadius(), irisSector.getOuterRadius(), irisSector.getDescription(), 1L);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        ArtifactDetailDto artifactDetailDto = new ArtifactDetailDto(1L, artifact.getName(), artifact.getDescription());
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");
        IrisFindingDetailDto detailDto = new IrisFindingDetailDto(1L, "imageId", sectorDetailDto, artifactDetailDto, irisFinding.getGeometryJson(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated());

        when(irisFindingRepository.findById(dto.id())).thenReturn(Optional.of(irisFinding));
        when(irisSectorRepository.getReferenceById(dto.irisSectorId())).thenReturn(irisSector);
        when(irisFindingMapper.toDetailDto(irisFinding)).thenReturn(detailDto);

        irisFindingService.update(dto);

        verify(irisFindingRepository).findById(dto.id());
        verify(irisSectorRepository).getReferenceById(dto.irisSectorId());
        verify(irisFindingMapper).toDetailDto(irisFinding);

        verifyNoMoreInteractions(irisFindingRepository);
        verifyNoMoreInteractions(irisSectorRepository);
        verifyNoMoreInteractions(irisFindingMapper);

        verifyNoInteractions(artifactRepository);
        verifyNoInteractions(irisImageRepository);
    }

    @Test
    void updateOnlyArtifactExecutes() {
        IrisFindingUpdateDto dto = new IrisFindingUpdateDto(1L, null, 1L, null, null);
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(1L, irisSector.getName(), irisSector.getStartAngle(), irisSector.getEndAngle(), irisSector.getInnerRadius(), irisSector.getOuterRadius(), irisSector.getDescription(), 1L);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        ArtifactDetailDto artifactDetailDto = new ArtifactDetailDto(1L, artifact.getName(), artifact.getDescription());
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");
        IrisFindingDetailDto detailDto = new IrisFindingDetailDto(1L, "imageId", sectorDetailDto, artifactDetailDto, irisFinding.getGeometryJson(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated());

        when(irisFindingRepository.findById(dto.id())).thenReturn(Optional.of(irisFinding));
        when(artifactRepository.getReferenceById(dto.artifactId())).thenReturn(artifact);
        when(irisFindingMapper.toDetailDto(irisFinding)).thenReturn(detailDto);

        irisFindingService.update(dto);

        verify(irisFindingRepository).findById(dto.id());
        verify(artifactRepository).getReferenceById(dto.artifactId());
        verify(irisFindingMapper).toDetailDto(irisFinding);

        verifyNoMoreInteractions(irisFindingRepository);
        verifyNoMoreInteractions(artifactRepository);
        verifyNoMoreInteractions(irisFindingMapper);

        verifyNoInteractions(irisSectorRepository);
        verifyNoInteractions(irisImageRepository);
    }

    @Test
    void updateOnlyGeometryJsonExecutes() {
        IrisFindingUpdateDto dto = new IrisFindingUpdateDto(1L, null, null, "newGeometryJson", null);
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(1L, irisSector.getName(), irisSector.getStartAngle(), irisSector.getEndAngle(), irisSector.getInnerRadius(), irisSector.getOuterRadius(), irisSector.getDescription(), 1L);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        ArtifactDetailDto artifactDetailDto = new ArtifactDetailDto(1L, artifact.getName(), artifact.getDescription());
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");
        IrisFindingDetailDto detailDto = new IrisFindingDetailDto(1L, "imageId", sectorDetailDto, artifactDetailDto, irisFinding.getGeometryJson(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated());

        when(irisFindingRepository.findById(dto.id())).thenReturn(Optional.of(irisFinding));
        when(irisFindingMapper.toDetailDto(irisFinding)).thenReturn(detailDto);

        irisFindingService.update(dto);

        verify(irisFindingRepository).findById(dto.id());
        verify(irisFindingMapper).toDetailDto(irisFinding);

        verifyNoMoreInteractions(irisFindingRepository);
        verifyNoMoreInteractions(irisFindingMapper);

        verifyNoInteractions(irisSectorRepository);
        verifyNoInteractions(irisImageRepository);
        verifyNoInteractions(artifactRepository);
    }

    @Test
    void updateOnlyIsValidatedExecutes() {
        IrisFindingUpdateDto dto = new IrisFindingUpdateDto(1L, null, null, null, true);
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        IrisSectorDetailDto sectorDetailDto = new IrisSectorDetailDto(1L, irisSector.getName(), irisSector.getStartAngle(), irisSector.getEndAngle(), irisSector.getInnerRadius(), irisSector.getOuterRadius(), irisSector.getDescription(), 1L);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        ArtifactDetailDto artifactDetailDto = new ArtifactDetailDto(1L, artifact.getName(), artifact.getDescription());
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");
        IrisFindingDetailDto detailDto = new IrisFindingDetailDto(1L, "imageId", sectorDetailDto, artifactDetailDto, irisFinding.getGeometryJson(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated());

        when(irisFindingRepository.findById(dto.id())).thenReturn(Optional.of(irisFinding));
        when(irisFindingMapper.toDetailDto(irisFinding)).thenReturn(detailDto);

        irisFindingService.update(dto);

        verify(irisFindingRepository).findById(dto.id());
        verify(irisFindingMapper).toDetailDto(irisFinding);

        verifyNoMoreInteractions(irisFindingRepository);
        verifyNoMoreInteractions(irisFindingMapper);

        verifyNoInteractions(irisSectorRepository);
        verifyNoInteractions(irisImageRepository);
        verifyNoInteractions(artifactRepository);
    }

    @Test
    void updateWithNonExistingIdThrows() {
        IrisFindingUpdateDto dto = new IrisFindingUpdateDto(1L, null, null, null, true);

        when(irisFindingRepository.findById(dto.id())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> irisFindingService.update(dto));

        verifyNoMoreInteractions(irisFindingRepository);

        verifyNoInteractions(irisFindingMapper);
        verifyNoInteractions(irisSectorRepository);
        verifyNoInteractions(irisImageRepository);
        verifyNoInteractions(artifactRepository);

        assertTrue(exception.getMessage().contains("Failed to fetch irisFinding from DB with id: " + dto.id()));
    }

    @Test
    void getAllExecutes() {
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");

        List<IrisFindingListDto> listDtoList = List.of(new IrisFindingListDto(1L, artifact.getName(), irisSector.getName(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated()));
        List<IrisFinding> irisFindings = List.of(irisFinding);

        when(irisFindingRepository.findAll()).thenReturn(irisFindings);
        when(irisFindingMapper.toListDtoList(irisFindings)).thenReturn(listDtoList);

        irisFindingService.getAll();

        verify(irisFindingRepository).findAll();
        verify(irisFindingMapper).toListDtoList(irisFindings);

        verifyNoMoreInteractions(irisFindingRepository);
        verifyNoMoreInteractions(irisFindingMapper);

        verifyNoInteractions(irisImageRepository);
        verifyNoInteractions(irisSectorRepository);
        verifyNoInteractions(artifactRepository);
    }

    @Test
    void getByIrisImageIdExecutes() {
        Long id = 1L;
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");

        List<IrisFindingListDto> listDtoList = List.of(new IrisFindingListDto(1L, artifact.getName(), irisSector.getName(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated()));
        List<IrisFinding> irisFindings = List.of(irisFinding);

        when(irisFindingRepository.findByIrisImage_Id(id)).thenReturn(irisFindings);
        when(irisFindingMapper.toListDtoList(irisFindings)).thenReturn(listDtoList);

        irisFindingService.getByIrisImageId(id);

        verify(irisFindingRepository).findByIrisImage_Id(id);
        verify(irisFindingMapper).toListDtoList(irisFindings);

        verifyNoMoreInteractions(irisFindingRepository);
        verifyNoMoreInteractions(irisFindingMapper);

        verifyNoInteractions(irisImageRepository);
        verifyNoInteractions(irisSectorRepository);
        verifyNoInteractions(artifactRepository);
    }

    @Test
    void getByArtifactLabelCodeExecutes() {
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");

        List<IrisFindingListDto> listDtoList = List.of(new IrisFindingListDto(1L, artifact.getName(), irisSector.getName(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated()));
        List<IrisFinding> irisFindings = List.of(irisFinding);

        when(irisFindingRepository.findByArtifact_LabelCode(artifact.getLabelCode())).thenReturn(irisFindings);
        when(irisFindingMapper.toListDtoList(irisFindings)).thenReturn(listDtoList);

        irisFindingService.getByArtifactLabelCode(artifact.getLabelCode());

        verify(irisFindingRepository).findByArtifact_LabelCode(artifact.getLabelCode());
        verify(irisFindingMapper).toListDtoList(irisFindings);

        verifyNoMoreInteractions(irisFindingRepository);
        verifyNoMoreInteractions(irisFindingMapper);

        verifyNoInteractions(irisImageRepository);
        verifyNoInteractions(irisSectorRepository);
        verifyNoInteractions(artifactRepository);
    }

    @Test
    void getByIsValidatedExecutes() {
        IrisImage irisImage = new IrisImage("imageUrl");
        IrisSector irisSector = new IrisSector("irisSectorName", 0.0, 360.0);
        Artifact artifact = new Artifact("artifactName", "artifactDescription", "ARTIFACT-001");
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");

        List<IrisFindingListDto> listDtoList = List.of(new IrisFindingListDto(1L, artifact.getName(), irisSector.getName(), irisFinding.getConfidenceScore(), irisFinding.getIsGenerated(), irisFinding.getIsValidated()));
        List<IrisFinding> irisFindings = List.of(irisFinding);

        when(irisFindingRepository.findByIsValidated(irisFinding.getIsValidated())).thenReturn(irisFindings);
        when(irisFindingMapper.toListDtoList(irisFindings)).thenReturn(listDtoList);

        irisFindingService.getByIsValidated(irisFinding.getIsValidated());

        verify(irisFindingRepository).findByIsValidated(irisFinding.getIsValidated());
        verify(irisFindingMapper).toListDtoList(irisFindings);

        verifyNoMoreInteractions(irisFindingRepository);
        verifyNoMoreInteractions(irisFindingMapper);

        verifyNoInteractions(irisImageRepository);
        verifyNoInteractions(irisSectorRepository);
        verifyNoInteractions(artifactRepository);
    }

    @Test
    void deleteExecutes() {
        Long id = 1L;

        irisFindingService.delete(id);

        verify(irisFindingRepository).deleteById(id);

        verifyNoMoreInteractions(irisFindingRepository);

        verifyNoInteractions(irisImageRepository);
        verifyNoInteractions(irisSectorRepository);
        verifyNoInteractions(artifactRepository);
        verifyNoInteractions(irisFindingMapper);
    }
}
