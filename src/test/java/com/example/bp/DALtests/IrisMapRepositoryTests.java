package com.example.bp.DALtests;

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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class IrisMapRepositoryTests {

    @Autowired
    EntityManager entityManager;

    @Autowired
    IrisMapRepository irisMapRepository;

    IrisMap firstIrisMapFirstIridologist;

    IrisMap secondIrisMapFirstIridologist;

    IrisMap firstIrisMapSecondIridiologist;

    IrisMap secondIrisMapSecondIridologist;

    Iridologist firstIridologist;

    Iridologist secondIridologist;

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

        assertThrows(DataIntegrityViolationException.class, () -> irisMapRepository.save(duplicateIrisMap));
    }
}
