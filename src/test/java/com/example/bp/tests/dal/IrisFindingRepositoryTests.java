package com.example.bp.tests.dal;

import com.example.bp.dal.entity.Artifact;
import com.example.bp.dal.entity.IrisFinding;
import com.example.bp.dal.entity.IrisImage;
import com.example.bp.dal.entity.IrisSector;
import com.example.bp.dal.repository.ArtifactRepository;
import com.example.bp.dal.repository.IrisFindingRepository;
import com.example.bp.dal.repository.IrisImageRepository;
import com.example.bp.dal.repository.IrisSectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class IrisFindingRepositoryTests {

    @Autowired
    IrisFindingRepository irisFindingRepository;

    @Autowired
    IrisImageRepository irisImageRepository;

    @Autowired
    IrisSectorRepository irisSectorRepository;

    @Autowired
    ArtifactRepository artifactRepository;

    @Autowired
    TestEntityManager entityManager;

    private IrisImage irisImage;
    private Artifact artifact;
    private IrisSector irisSector;
    private IrisFinding irisFinding;

    @BeforeEach
    void setUp() {
        irisImage = new IrisImage("imageUrl");
        artifact = new Artifact("artifact", "artifactDescription", "ARTIFACT-00");
        irisSector = new IrisSector("irisSector", 25.0, 360.0);
        irisFinding = new IrisFinding(irisImage, irisSector, artifact, "geometryJson");

        irisImage.addFinding(irisFinding);

        entityManager.persist(irisImage);
        entityManager.persist(artifact);
        entityManager.persist(irisSector);
        entityManager.persist(irisFinding);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void createIrisFinding() {
        IrisFinding irisFinding = new IrisFinding(irisImage, irisSector, artifact, "anotherGeometryJson");

        assertDoesNotThrow(() -> irisFindingRepository.saveAndFlush(irisFinding));
    }

    @Test
    void getExistingIrisFindingById() {
        IrisFinding foundIrisFinding = irisFindingRepository.findById(irisFinding.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisFinding from DB"));

        assertEquals(irisFinding, foundIrisFinding);
    }

    @Test
    void getNonExistentIrisFindingById() {
        Optional<IrisFinding> foundIrisFinding = irisFindingRepository.findById((long) 9999);

        assertTrue(foundIrisFinding.isEmpty());
    }

    @Test
    void getIrisFindingByImage() {
        List<IrisFinding> foundIrisFindings = irisFindingRepository.findByIrisImage_Id(irisImage.getId());

        assertTrue(foundIrisFindings.contains(irisFinding));
    }
}
