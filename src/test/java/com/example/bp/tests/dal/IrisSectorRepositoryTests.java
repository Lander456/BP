package com.example.bp.tests.dal;

import com.example.bp.dal.entity.IrisMap;
import com.example.bp.dal.entity.IrisSector;
import com.example.bp.dal.repository.IrisMapRepository;
import com.example.bp.dal.repository.IrisSectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class IrisSectorRepositoryTests {

    @Autowired
    private IrisSectorRepository irisSectorRepository;

    @Autowired
    private IrisMapRepository irisMapRepository;

    @Autowired
    private TestEntityManager entityManager;

    private IrisSector irisSector1;

    private IrisMap irisMap1;

    @BeforeEach
    void setUp() {
        irisSector1 = new IrisSector("irisSectorOne", 0.0, 270.0);
        irisMap1 = new IrisMap("imageUrl");
        irisMap1.setStoragePath("firstMapStoragePath");

        irisSector1.setIrisMap(irisMap1);
        irisMap1.setSectors(new ArrayList<IrisSector>(List.of(irisSector1)));

        entityManager.persist(irisMap1);
        entityManager.persist(irisSector1);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void createIrisSector() {
        IrisSector irisSector = new IrisSector("newIrisSector", 0.0, 135.0);

        assertDoesNotThrow(() -> irisSectorRepository.saveAndFlush(irisSector));
    }

    @Test
    void getExistingIrisSector() {
        IrisSector irisSector = irisSectorRepository.findById(irisSector1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisSector from DB"));

        assertEquals(irisSector1, irisSector);
    }

    @Test
    void getNonExistentIrisSector() {
        Optional<IrisSector> irisSector = irisSectorRepository.findById((long) 9999);

        assertTrue(irisSector.isEmpty());
    }

    @Test
    void getIrisSectorByExistingMapIdAndName() {
        IrisSector irisSector = irisSectorRepository.findByIrisMapIdAndName(irisSector1.getIrisMap().getId(), irisSector1.getName())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisSector from DB"));

        assertEquals(irisSector1, irisSector);
    }

    @Test
    void getIrisSectorByNonExistentMapIdAndName() {
        Optional<IrisSector> irisSector = irisSectorRepository.findByIrisMapIdAndName((long) 9999, "madeUpName");

        assertTrue(irisSector.isEmpty());
    }

    @Test
    void getIrisSectorByNonExistentMapIdAndExistingName() {
        Optional<IrisSector> irisSector = irisSectorRepository.findByIrisMapIdAndName((long) 9999, irisSector1.getName());

        assertTrue(irisSector.isEmpty());
    }

    @Test
    void getIrisSectorByExistingMapIdAndNonExistentName() {
        Optional<IrisSector> irisSector = irisSectorRepository.findByIrisMapIdAndName(irisSector1.getIrisMap().getId(), "madeUpName");

        assertTrue(irisSector.isEmpty());
    }

    @Test
    void getExistingIrisSectorByMapId() {
        List<IrisSector> irisSectors = irisSectorRepository.findByIrisMapId(irisMap1.getId());

        assertTrue(irisSectors.contains(irisSector1));
    }

    @Test
    void updateIrisSectorName() {
        irisSector1.setName("newName");
        irisSectorRepository.saveAndFlush(irisSector1);

        IrisSector irisSectorToCompare = irisSectorRepository.findById(irisSector1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisSector from DB"));
        assertEquals("newName", irisSectorToCompare.getName());
    }

    @Test
    void updateIrisSectorDescription() {
        irisSector1.setDescription("newDescription");
        irisSectorRepository.saveAndFlush(irisSector1);

        IrisSector irisSectorToCompare = irisSectorRepository.findById(irisSector1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisSector from DB"));
        assertEquals("newDescription", irisSectorToCompare.getDescription());
    }

    @Test
    void updateIrisSectorStartAngle() {
        irisSector1.setStartAngle(25.0);
        irisSectorRepository.saveAndFlush(irisSector1);

        IrisSector irisSectorToCompare = irisSectorRepository.findById(irisSector1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisSector from DB"));
        assertEquals(25.0, irisSectorToCompare.getStartAngle());
    }

    @Test
    void updateIrisSectorEndAngle() {
        irisSector1.setEndAngle(40.0);
        irisSectorRepository.saveAndFlush(irisSector1);

        IrisSector irisSectorToCompare = irisSectorRepository.findById(irisSector1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisSector from DB"));
        assertEquals(40.0, irisSectorToCompare.getEndAngle());
    }

    @Test
    void updateIrisSectorIrisMap() {

        IrisMap newIrisMap = new IrisMap("newIrisMapUrl");
        newIrisMap.setStoragePath("newStoragePath");
        irisMapRepository.saveAndFlush(newIrisMap);

        irisSector1.setIrisMap(newIrisMap);
        irisSectorRepository.saveAndFlush(irisSector1);

        IrisSector irisSectorToCompare = irisSectorRepository.findById(irisSector1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisSector from DB"));
        assertEquals(newIrisMap, irisSectorToCompare.getIrisMap());
    }

    @Test
    void deleteExistingIrisSector() {
        irisSectorRepository.delete(irisSector1);
        irisSectorRepository.flush();

        Optional<IrisSector> irisSector = irisSectorRepository.findById(irisSector1.getId());
        assertTrue(irisSector.isEmpty());
    }
}
