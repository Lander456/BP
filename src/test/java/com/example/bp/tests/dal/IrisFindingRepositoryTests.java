package com.example.bp.tests.dal;

import com.example.bp.dal.entity.Artifact;
import com.example.bp.dal.entity.IrisFinding;
import com.example.bp.dal.entity.IrisImage;
import com.example.bp.dal.entity.IrisSector;
import com.example.bp.dal.repository.IrisFindingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class IrisFindingRepositoryTests {

    @Autowired
    IrisFindingRepository irisFindingRepository;

    @Autowired
    TestEntityManager entityManager;

    private IrisImage irisImage;
    private Artifact artifact1;
    private Artifact artifact2;
    private IrisSector irisSector;
    private IrisFinding irisFindingTrue;
    private IrisFinding irisFindingFalse;

    @BeforeEach
    void setUp() {
        irisImage = new IrisImage("imageUrl");
        irisImage.setStoragePath("imageStoragePath");
        irisImage.setUploadedAt(LocalDateTime.now());

        artifact1 = new Artifact("artifact1", "artifact1Description", "ARTIFACT-01");
        artifact2 = new Artifact("artifact2", "artifact2Description", "ARTIFACT-02");
        irisSector = new IrisSector("irisSector", 25.0, 360.0);
        irisFindingTrue = new IrisFinding(irisImage, irisSector, artifact1, "geometryJson");
        irisFindingFalse = new IrisFinding(irisImage, irisSector, artifact2, "geometryJson");
        irisFindingFalse.setIsValidated(false);


        irisImage.addFinding(irisFindingTrue);

        entityManager.persist(irisImage);
        entityManager.persist(artifact1);
        entityManager.persist(artifact2);
        entityManager.persist(irisSector);
        entityManager.persist(irisFindingTrue);
        entityManager.persist(irisFindingFalse);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void createIrisFinding() {
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact1, "anotherGeometryJson");

        assertDoesNotThrow(() -> irisFindingRepository.saveAndFlush(irisFinding));
    }

    @Test
    void getExistingIrisFindingById() {
        IrisFinding foundIrisFinding = irisFindingRepository.findById(irisFindingTrue.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisFinding from DB"));

        assertEquals(irisFindingTrue, foundIrisFinding);
    }

    @Test
    void getNonExistentIrisFindingById() {
        Optional<IrisFinding> foundIrisFinding = irisFindingRepository.findById((long) 9999);

        assertTrue(foundIrisFinding.isEmpty());
    }

    @Test
    void getIrisFindingByImage() {
        List<IrisFinding> foundIrisFindings = irisFindingRepository.findByIrisImage_Id(irisImage.getId());

        assertTrue(foundIrisFindings.containsAll(List.of(irisFindingTrue, irisFindingFalse)));
    }

    @Test
    void getIrisFindingByValidatedFalse() {
        List<IrisFinding> foundIrisFindings = irisFindingRepository.findByIsValidated(false);

        assertThat(foundIrisFindings).containsExactly(irisFindingFalse);
    }

    @Test
    void getIrisFindingByValidatedTrue() {
        List<IrisFinding> foundIrisFindings = irisFindingRepository.findByIsValidated(true);

        assertThat(foundIrisFindings).contains(irisFindingTrue);
    }

    @Test
    void getIrisFindingByArtifactLabelCode() {
        List<IrisFinding> foundIrisFindings = irisFindingRepository.findByArtifact_LabelCode(artifact1.getLabelCode());

        assertThat(foundIrisFindings).containsExactly(irisFindingTrue);
    }

    @Test
    void updateConfidenceScore() {
        irisFindingTrue.setConfidenceScore(0.95);
        irisFindingRepository.saveAndFlush(irisFindingTrue);

        IrisFinding foundIrisFinding = irisFindingRepository.findById(irisFindingTrue.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisFinding from DB"));
        assertEquals(0.95, foundIrisFinding.getConfidenceScore());
    }

    @Test
    void updateValidation() {
        irisFindingFalse.setIsValidated(true);
        irisFindingRepository.saveAndFlush(irisFindingFalse);

        IrisFinding foundIrisFinding = irisFindingRepository.findById(irisFindingFalse.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisFinding from DB"));
        assertTrue(foundIrisFinding.getIsValidated());
    }

    @Test
    void deleteIrisFinding() {
        irisFindingRepository.delete(irisFindingTrue);
        irisFindingRepository.flush();

        Optional<IrisFinding> foundIrisFinding = irisFindingRepository.findById(irisFindingTrue.getId());
        assertTrue(foundIrisFinding.isEmpty());
    }
}
