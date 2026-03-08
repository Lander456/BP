package com.example.bp.tests.dal;

import com.example.bp.dal.entity.Artifact;
import com.example.bp.dal.repository.ArtifactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ArtifactRepositoryTests {

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Artifact artifact1;

    @BeforeEach
    void setUp() {
        artifact1 = new Artifact("firstArtifact", "descriptionOfFirstArtifact", "ARTIFACT-01");

        entityManager.persist(artifact1);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void saveArtifact() {
        Artifact artifact = new Artifact("newArtifact", "newArtifactDescription", "ARTIFACT-03");

        assertDoesNotThrow(() -> artifactRepository.saveAndFlush(artifact));
    }

    @Test
    void saveArtifactWithDuplicateLabelCode() {
        Artifact artifact = new Artifact("newArtifact", "newArtifactDescription", artifact1.getLabelCode());

        assertThrows(DataIntegrityViolationException.class, () -> artifactRepository.saveAndFlush(artifact));
    }

    @Test
    void getExistingArtifact() {
        Artifact artifact = artifactRepository.findById(artifact1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch artifact from DB"));

        assertEquals(artifact1, artifact);
    }

    @Test
    void getNonExistentArtifactById() {
        Optional<Artifact> artifact = artifactRepository.findById((long) 9999);

        assertTrue(artifact.isEmpty());
    }

    @Test
    void getArtifactByLabelCode() {
        Artifact artifact = artifactRepository.findByLabelCode(artifact1.getLabelCode())
                .orElseThrow(() -> new AssertionError("Failed to fetch artifact from DB"));

        assertEquals(artifact1, artifact);
    }

    @Test
    void getNonExistentArtifactByLabelCode() {
        Optional<Artifact> artifact = artifactRepository.findByLabelCode("madeUpLabelCode");

        assertTrue(artifact.isEmpty());
    }

    @Test
    void getArtifactByName() {
        List<Artifact> artifacts = artifactRepository.findByName(artifact1.getName());

        assertTrue(artifacts.contains(artifact1));
    }

    @Test
    void getNonExistentArtifactByName() {
        List<Artifact> artifacts = artifactRepository.findByName("madeUpName");

        assertTrue(artifacts.isEmpty());
    }

    @Test
    void updateArtifactName() {
        artifact1.setName("newName");

        artifactRepository.saveAndFlush(artifact1);
        Artifact artifactToCompare = artifactRepository.findById(artifact1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch artifact from DB"));

        assertEquals("newName", artifactToCompare.getName());
    }

    @Test
    void updateArtifactDescription() {
        artifact1.setDescription("newDescription");

        artifactRepository.saveAndFlush(artifact1);
        Artifact artifactToCompare = artifactRepository.findById(artifact1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch artifact from DB"));

        assertEquals("newDescription", artifactToCompare.getDescription());
    }

    @Test
    void deleteExistingArtifact() {
        artifactRepository.delete(artifact1);
        artifactRepository.flush();

        Optional<Artifact> artifact = artifactRepository.findById(artifact1.getId());
        assertTrue(artifact.isEmpty());
    }
}
