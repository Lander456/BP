package com.example.bp.tests.DAL;

import com.example.bp.DAL.model.Iridologist;
import com.example.bp.DAL.model.IrisMap;
import com.example.bp.DAL.repository.IrisMapRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class IrisMapRepositoryTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private IrisMapRepository irisMapRepository;

    private IrisMap firstIrisMapFirstIridologist;

    private IrisMap secondIrisMapFirstIridologist;

    private IrisMap firstIrisMapSecondIridiologist;

    private IrisMap secondIrisMapSecondIridologist;

    private Iridologist firstIridologist;

    private Iridologist secondIridologist;

    @BeforeEach
    void setUp() {
        firstIridologist = new Iridologist("John", "Doe");
        secondIridologist = new Iridologist("Jane", "Doe");
        firstIrisMapFirstIridologist = new IrisMap("firstIrisMapUrl");
        secondIrisMapFirstIridologist = new IrisMap("secondIrisMapUrl");
        firstIrisMapSecondIridiologist = new IrisMap("thirdIrisMapUrl");
        secondIrisMapSecondIridologist = new IrisMap("fourthIrisMapUrl");

        firstIrisMapFirstIridologist.setIridologist(firstIridologist);
        secondIrisMapFirstIridologist.setIridologist(secondIridologist);
        firstIrisMapSecondIridiologist.setIridologist(secondIridologist);
        secondIrisMapSecondIridologist.setIridologist(secondIridologist);

        entityManager.persist(firstIridologist);
        entityManager.persist(secondIridologist);
        entityManager.persist(firstIrisMapFirstIridologist);
        entityManager.persist(secondIrisMapFirstIridologist);
        entityManager.persist(firstIrisMapSecondIridiologist);
        entityManager.persist(secondIrisMapSecondIridologist);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void saveNewIrisMap() {
        IrisMap newIrisMap = new IrisMap("newIrisMapLink");

        IrisMap savedIrisMap = irisMapRepository.save(newIrisMap);

        assertEquals(newIrisMap, savedIrisMap);
    }

    @Test
    void saveDuplicateIrisMap() {
        IrisMap duplicateIrisMap = new IrisMap(firstIrisMapFirstIridologist.getImageUrl());
        duplicateIrisMap.setIridologist(firstIridologist);

        assertThrows(DataIntegrityViolationException.class, () -> irisMapRepository.saveAndFlush(duplicateIrisMap));
    }

    @Test
    void getExistingIrisMap() {
        IrisMap fetchedIrisMap = irisMapRepository.findById(firstIrisMapFirstIridologist.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch IrisMap from DB"));

        assertEquals(firstIrisMapFirstIridologist, fetchedIrisMap);
    }

    @Test
    void getNonExistentIrisMap() {
        Optional<IrisMap> nonExistentIrisMap = irisMapRepository.findById(333L);

        assertTrue(nonExistentIrisMap.isEmpty());
    }

    @Test
    void updateExistingIrisMap() {
        IrisMap editedIrisMap = irisMapRepository.findById(firstIrisMapFirstIridologist.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch IrisMap from DB"));

        editedIrisMap.setIridologist(secondIridologist);
        irisMapRepository.saveAndFlush(editedIrisMap);

        IrisMap fetchedIrisMap = irisMapRepository.findById(firstIrisMapFirstIridologist.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch IrisMap from DB"));
        assertEquals(editedIrisMap, fetchedIrisMap);
    }

    @Test
    void deleteIrisMap() {
        irisMapRepository.delete(secondIrisMapSecondIridologist);

        Optional<IrisMap> fetchedIrisMap = irisMapRepository.findById(secondIrisMapSecondIridologist.getId());

        assertTrue(fetchedIrisMap.isEmpty());
    }
}
