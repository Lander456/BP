package com.example.bp.tests.service;

import com.example.bp.api.dto.ArtifactDetailDto;
import com.example.bp.api.mapper.ArtifactMapper;
import com.example.bp.dal.entity.Artifact;
import com.example.bp.dal.repository.ArtifactRepository;
import com.example.bp.service.impl.ArtifactServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtifactServiceTests {

    @Mock
    private ArtifactRepository artifactRepository;

    @Mock
    private ArtifactMapper artifactMapper;

    private ArtifactServiceImpl artifactService;

    @BeforeEach
    void setUp() {
        artifactService = new ArtifactServiceImpl(artifactRepository, artifactMapper);
    }

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
        when(artifactMapper.toDetailDto(existingArtifact)).thenReturn(new ArtifactDetailDto(1L, existingArtifact.getName(), existingArtifact.getDescription()));

        artifactService.getByLabelCode(labelCode);

        verify(artifactRepository).findByLabelCode(labelCode);
        verify(artifactMapper).toDetailDto(existingArtifact);
    }
}
