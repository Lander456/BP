package com.example.bp.tests.dal;

import com.example.bp.dal.entity.Iridologist;
import com.example.bp.dal.entity.IrisMap;
import com.example.bp.dal.repository.IrisMapRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class IrisMapRepositoryTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private IrisMapRepository irisMapRepository;

    /// predeclared IrisMap instance, defined inside the setUp method for use in tests
    private IrisMap irisMap1;

    /// predeclared IrisMap instance, defined inside the setUp method for use in tests
    private IrisMap irisMap2;

    /// predeclared IrisMap instance, defined inside the setUp method for use in tests
    private IrisMap irisMap3;

    /// predeclared IrisMap instance, defined inside the setUp method for use in tests
    private IrisMap irisMap4;

    /// predeclared Iridologist instance, defined inside the setUp method for use in tests
    private Iridologist firstIridologist;

    /// predeclared Iridologist instance, defined inside the setUp method for use in tests
    private Iridologist secondIridologist;

    /// setup method executed before each test to ensure database is reset to this state and populated with certain data
    @BeforeEach
    void setUp() {
        firstIridologist = new Iridologist("John", "Doe", "johndoe", "interestingpass");
        secondIridologist = new Iridologist("Jane", "Doe", "janedoe", "interestingpass");
        irisMap1 = new IrisMap("firstIrisMapUrl");
        irisMap2 = new IrisMap("secondIrisMapUrl");
        irisMap3 = new IrisMap("thirdIrisMapUrl");
        irisMap4 = new IrisMap("fourthIrisMapUrl");

        irisMap1.setStoragePath("firstMapStoragePath");
        irisMap2.setStoragePath("secondMapStoragePath");
        irisMap3.setStoragePath("thirdMapStoragePath");
        irisMap4.setStoragePath("fourthMapStoragePath");

        irisMap1.setIridologist(firstIridologist);
        irisMap2.setIridologist(firstIridologist);
        irisMap3.setIridologist(secondIridologist);
        irisMap4.setIridologist(secondIridologist);

        entityManager.persist(firstIridologist);
        entityManager.persist(secondIridologist);
        entityManager.persist(irisMap1);
        entityManager.persist(irisMap2);
        entityManager.persist(irisMap3);
        entityManager.persist(irisMap4);

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * This test attempts to save a new, unique IrisMap instance into the database
     *
     * @see IrisMap
     * @see IrisMapRepository
     * @throws AssertionError if the saved instance fails to get saved in the database
     */
    @Test
    void saveNewIrisMap() {
        IrisMap newIrisMap = new IrisMap("newIrisMapLink");
        newIrisMap.setStoragePath("someStoragePath");

        IrisMap savedIrisMap = irisMapRepository.save(newIrisMap);

        assertEquals(newIrisMap, savedIrisMap);
    }

    /**
     * This test attempts to save a new, duplicate IrisMap instance into the database, which should throw
     *
     * @throws AssertionError if the attempt to save a duplicate IrisMap does not throw and instead succeeds
     * @see IrisMap
     * @see IrisMapRepository
     */
    @Test
    void saveDuplicateIrisMap() {
        IrisMap duplicateIrisMap = new IrisMap(irisMap1.getImageUrl());
        duplicateIrisMap.setIridologist(firstIridologist);
        duplicateIrisMap.setStoragePath("duplicateStoragePath");

        assertThrows(DataIntegrityViolationException.class, () -> irisMapRepository.saveAndFlush(duplicateIrisMap));
    }

    /**
     * This test attempts to fetch an already saved IrisMap instance from the database
     *
     * @throws AssertionError if the test failed to fetch the IrisMap (it either couldn't find it in the database or
     * something else went wrong during the process) or it throws if the fetched IrisMap is not the same as the IrisMap
     * it should've fetched
     * @see IrisMap
     * @see IrisMapRepository
     */
    @Test
    void getExistingIrisMap() {
        IrisMap fetchedIrisMap = irisMapRepository.findById(irisMap1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch IrisMap from DB"));

        assertEquals(irisMap1, fetchedIrisMap);
    }

    /**
     * This test attempts to fetch an IrisMap that is not in the database, namely by attempting to fetch an ID that is not
     * present
     *
     * @throws AssertionError if it actually fetches something
     * @see IrisMap
     * @see IrisMapRepository
     */
    @Test
    void getNonExistentIrisMap() {
        Optional<IrisMap> nonExistentIrisMap = irisMapRepository.findById(333L);

        assertTrue(nonExistentIrisMap.isEmpty());
    }

    @Test
    void getIrisMapByIridologist() {
        List<IrisMap> irisMaps = irisMapRepository.findByIridologistId(firstIridologist.getId());

        assertThat(irisMaps).containsExactlyInAnyOrder(irisMap1, irisMap2);
    }

    @Test
    void getIrisMapByImageUrl() {
        IrisMap irisMap = irisMapRepository.findByImageUrl(irisMap1.getImageUrl())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisMap from DB"));

        assertEquals(irisMap1, irisMap);
    }

    @Test
    void getNonExistentIrisMapByImageUrl() {
        Optional<IrisMap> irisMap = irisMapRepository.findByImageUrl("madeUpImageUrl");

        assertTrue(irisMap.isEmpty());
    }

    @Test
    void tryExistsWithExistingIrisMap() {
        assertTrue(irisMapRepository.existsByImageUrl(irisMap1.getImageUrl()));
    }

    @Test
    void tryExistsWithNonExistentIrisMap() {
        assertFalse(irisMapRepository.existsByImageUrl("madeUpImageUrl"));
    }

    /**
     * This test attempts to update an IrisMap in the database
     *
     * @throws AssertionError if it fails to fetch an irisMap from the DB or when it fails to properly update the irisMap
     * @see IrisMap
     * @see IrisMapRepository
     */
    @Test
    void updateExistingIrisMap() {
        IrisMap editedIrisMap = irisMapRepository.findById(irisMap1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch IrisMap from DB"));

        editedIrisMap.setIridologist(secondIridologist);
        irisMapRepository.saveAndFlush(editedIrisMap);

        IrisMap fetchedIrisMap = irisMapRepository.findById(irisMap1.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch IrisMap from DB"));
        assertEquals(editedIrisMap, fetchedIrisMap);
    }

    /**
     * This test attempts to delete an IrisMap from the database
     *
     * @throws AssertionError if it fails to delete and fetches the IrisMap after its deletion
     * @see IrisMap
     * @see IrisMapRepository
     */
    @Test
    void deleteIrisMap() {
        irisMapRepository.delete(irisMap4);

        Optional<IrisMap> fetchedIrisMap = irisMapRepository.findById(irisMap4.getId());

        assertTrue(fetchedIrisMap.isEmpty());
    }
}
