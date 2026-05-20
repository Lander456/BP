package com.example.bp.tests.service;

import com.example.bp.api.dto.ArtifactCreateDto;
import com.example.bp.api.dto.ArtifactDetailDto;
import com.example.bp.api.dto.ArtifactListDto;
import com.example.bp.api.dto.ArtifactUpdateDto;
import com.example.bp.api.mapper.ArtifactMapper;
import com.example.bp.dal.entity.Artifact;
import com.example.bp.dal.repository.ArtifactRepository;
import com.example.bp.service.impl.ArtifactServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtifactServiceTests {

    @Mock
    private ArtifactRepository artifactRepository;

    @Mock
    private ArtifactMapper artifactMapper;

    @InjectMocks
    private ArtifactServiceImpl artifactService;

    @Test
    void getExistingArtifactById() {
        Long id = 1L;
        Artifact existingArtifact = new Artifact("existingArtifact", "existingArtifactDescription", "SYS-ARTIFACT-01");

        when(artifactRepository.findById(id)).thenReturn(Optional.of(existingArtifact));
        when(artifactMapper.toDetailDto(existingArtifact)).thenReturn(new ArtifactDetailDto(id, existingArtifact.getName(), existingArtifact.getDescription()));

        artifactService.getById(id);

        verify(artifactRepository).findById(id);
        verify(artifactMapper).toDetailDto(existingArtifact);
    }

    @Test
    void getNonExistentArtifactById() {
        Long id = 9999L;
        when(artifactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            artifactService.getById(id);
        });

        verifyNoInteractions(artifactMapper);
    }

    @Test
    void getExistingArtifactByLabelCode() {
        String labelCode = "SYS-ARTIFACT-01";
        Artifact existingArtifact = new Artifact("existingArtifact", "existingArtifactDescription", labelCode);

        when(artifactRepository.findByLabelCode(labelCode)).thenReturn(Optional.of(existingArtifact));
        when(artifactMapper.toDetailDto(existingArtifact)).thenReturn(new ArtifactDetailDto((Long) 1L, existingArtifact.getName(), existingArtifact.getDescription()));

        artifactService.getByLabelCode(labelCode);

        verify(artifactRepository).findByLabelCode(labelCode);
        verify(artifactMapper).toDetailDto(existingArtifact);
    }

    @Test
    void getNonExistentArtifactByLabelCode() {
        String labelCode = "madeUp";

        when(artifactRepository.findByLabelCode(labelCode)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            artifactService.getByLabelCode(labelCode);
        });

        verifyNoInteractions(artifactMapper);
    }

    @Test
    void createNewArtifact() {
        ArtifactCreateDto dto = new ArtifactCreateDto("newArtifactName", "newArtifactDescription", "USR-NEW-ARTIFACT");
        Artifact newArtifact = new Artifact(dto.name(), dto.description(), dto.labelCode());
        newArtifact.setId(1L);

        when(artifactMapper.toEntity(dto)).thenReturn(newArtifact);
        when(artifactRepository.save(newArtifact)).thenReturn(newArtifact);
        when(artifactMapper.toDetailDto(newArtifact)).thenReturn(new ArtifactDetailDto(newArtifact.getId(), newArtifact.getName(), newArtifact.getDescription()));

        artifactService.create(dto);

        verify(artifactMapper).toEntity(dto);
        verify(artifactRepository).save(newArtifact);
        verify(artifactMapper).toDetailDto(newArtifact);

        verifyNoMoreInteractions(artifactMapper);
        verifyNoMoreInteractions(artifactRepository);
    }

    @Test
    void createDuplicateLabelCodeArtifact() {
        ArtifactCreateDto dto = new ArtifactCreateDto("duplicateArtifactName", "duplicateArtifactDescription", "duplicateLabelCode");
        Artifact duplicateArtifact = new Artifact(dto.name(), dto.description(), dto.labelCode());

        when(artifactMapper.toEntity(dto)).thenReturn(duplicateArtifact);
        when(artifactRepository.save(duplicateArtifact)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> {
            artifactService.create(dto);
        });

        verify(artifactMapper).toEntity(dto);
        verify(artifactRepository).save(duplicateArtifact);

        verifyNoMoreInteractions(artifactMapper);
        verifyNoMoreInteractions(artifactRepository);
    }

    @Test
    void updateExistingArtifact() {
        ArtifactUpdateDto dto = new ArtifactUpdateDto(1L, "newName", "newDescription");
        Artifact existingArtifact = new Artifact("existingArtifactName", "existingArtifactDescription", "SYS-ARTIFACT-01");
        Artifact artifactUpdate = new Artifact(dto.name(), dto.description(), null);
        Artifact updatedArtifact = new Artifact(dto.name(), dto.description(), existingArtifact.getLabelCode());

        when(artifactMapper.toEntity(dto)).thenReturn(artifactUpdate);
        when(artifactRepository.save(artifactUpdate)).thenReturn(updatedArtifact);
        when(artifactMapper.toDetailDto(updatedArtifact)).thenReturn(new ArtifactDetailDto((Long) 1L, updatedArtifact.getName(), updatedArtifact.getDescription()));

        artifactService.update(dto);

        verify(artifactMapper).toEntity(dto);
        verify(artifactRepository).save(artifactUpdate);

        verifyNoMoreInteractions(artifactMapper);
        verifyNoMoreInteractions(artifactRepository);
    }

    @Test
    void getExistingArtifactByName() {
        String name = "existingArtifactName";
        Artifact existingArtifact = new Artifact(name, "existingArtifactDescription", "SYS-ARTIFACT-01");
        List<Artifact> foundArtifacts = List.of(existingArtifact);

        when(artifactRepository.findByName(name)).thenReturn(foundArtifacts);

        artifactService.getByName(name);

        verify(artifactRepository).findByName(name);
        verify(artifactMapper).toListDtoList(foundArtifacts);

        verifyNoMoreInteractions(artifactRepository);
        verifyNoMoreInteractions(artifactMapper);
    }

    @Test
    void getAllArtifacts() {
        Artifact existingArtifact = new Artifact("existingArtifactName", "existingArtifactDescription", "SYS-ARTIFACT-01");
        List<Artifact> foundArtifacts = List.of(existingArtifact);

        when(artifactRepository.findAll()).thenReturn(foundArtifacts);

        artifactService.getAll();

        verify(artifactRepository).findAll();
        verify(artifactMapper).toListDtoList(foundArtifacts);

        verifyNoMoreInteractions(artifactRepository);
        verifyNoMoreInteractions(artifactMapper);
    }

    @Test
    void deleteExistingArtifact() {
        ArtifactListDto dto = new ArtifactListDto(1L, "artifactName");

        artifactService.delete(dto.id());

        verify(artifactRepository).deleteById(dto.id());

        verifyNoInteractions(artifactMapper);
        verifyNoMoreInteractions(artifactRepository);
    }
}
