package com.example.bp.tests.dal;

import com.example.bp.dal.entity.Iridologist;
import com.example.bp.dal.repository.IridologistRepository;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class IridologistRepositoryTests {

    @Autowired
    private IridologistRepository iridologistRepository;

    @Autowired
    private TestEntityManager entityManager;

    /// private Iridologist instance, predefined in the setUp method for use in the tests
    private Iridologist janeDoe;

    /// private Iridologist instance, predefined in the setUp method for use in the tests
    private Iridologist johnDoe;

    /// private Iridologist instance, predefined in the setUp method for use in the tests
    private Iridologist johnHamcock;

    /// setup method executed before each test to ensure database is reset to this state and populated with certain data
    @BeforeEach
    void setUp() {
        janeDoe = new Iridologist("Jane", "Doe", "janedoe", "interestingpass");
        johnDoe = new Iridologist("John", "Doe", "johndoe", "interestingpass");
        johnHamcock = new Iridologist("John", "Hamcock", "johnhamcock", "interestingpass");

        entityManager.persist(johnDoe);
        entityManager.persist(janeDoe);
        entityManager.persist(johnHamcock);

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * This test attempts to save an Iridologist instance into the database and then find it in the database using the
     * IridologistRepository
     *
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void saveIridologist() {
        Iridologist iridologist = new Iridologist("Jeffrey", "Doe", "jeffreydoe", "interestingpass");

        iridologistRepository.saveAndFlush(iridologist);

        List<Iridologist> found = iridologistRepository.findAll();
        assertThat(found).extracting(Iridologist::getFirstName, Iridologist::getLastName)
                .contains(tuple("Jeffrey", "Doe"));
    }

    /**
     * This test attempts to find all the instances of Iridologist with the first name Jane from janeDoe, defined above
     * attempts all this using IridologistRepository
     *
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void findIridologistByFirstName() {
        List<Iridologist> found = iridologistRepository.findByFirstName(janeDoe.getFirstName());
        assertThat(found).extracting(Iridologist::getFirstName, Iridologist::getLastName)
                .containsExactly(tuple(janeDoe.getFirstName(), janeDoe.getLastName()));
    }

    /**
     * This test attempts to find all the Iridologist instances with a certain name using the IridologistRepository
     *
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void findMultipleIridologistsByFirstName() {
        List<Iridologist> found = iridologistRepository.findByFirstName("John");
        assertThat(found).extracting(Iridologist::getFirstName, Iridologist::getLastName)
                .contains(
                        tuple("John", "Doe"),
                        tuple("John", "Hamcock"));
    }

    /**
     * This test attempts to find an iridologist in the database by a fixed id (1) using the IridologistRepository
     *
     * @throws org.opentest4j.AssertionFailedError when the test fails
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void findById() {
        Iridologist iridologist = new Iridologist("irrelevant", "man", "irrelevantman", "interestingpass");
        Iridologist saved = iridologistRepository.saveAndFlush(iridologist);

        Optional<Iridologist> found = iridologistRepository.findById(saved.getId());
        assertTrue(found.isPresent());
    }

    /**
     * This test attempts to update an Iridologist's first name and save it in the database, all using the IridologistRepository
     *
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void updateExistingFirstName() {
        List<Iridologist> foundToEdit = iridologistRepository.findByFirstName(janeDoe.getFirstName());
        Iridologist iridologist = foundToEdit.getFirst();

        iridologist.setFirstName("Amelie");
        iridologistRepository.saveAndFlush(iridologist);

        List<Iridologist> foundToCompare = iridologistRepository.findByFirstName("Amelie");
        assertEquals(foundToCompare.getFirst(), iridologist);
    }

    /**
     * This tests the findByLastName method of the {@link IridologistRepository} it tries to find all the Iridologists
     * with a certain last name
     *
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void findBySurname() {
        List<Iridologist> found = iridologistRepository.findByLastName(johnDoe.getLastName());

        assertThat(found).extracting(Iridologist::getFirstName, Iridologist::getLastName)
                .containsExactlyInAnyOrder(
                        tuple(johnDoe.getFirstName(), johnDoe.getLastName()),
                        tuple(janeDoe.getFirstName(), janeDoe.getLastName())
                );
    }

    /**
     * This test attempts to update an Iridologist's first name and save it in the database, all using the IridologistRepository
     *
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void updateExistingSurname() {
        List<Iridologist> foundToEdit = iridologistRepository.findByLastName(johnHamcock.getLastName());
        Iridologist iridologist = foundToEdit.getFirst();

        iridologist.setLastName("interestingLastName");
        Iridologist savedIridologist = iridologistRepository.saveAndFlush(iridologist);

        Iridologist foundToCompare = iridologistRepository.findById(savedIridologist.getId())
                .orElseThrow(() -> new AssertionError("Iridologist not found in DB"));

        assertEquals(foundToCompare, iridologist);
    }

    /**
     * This test attempts to deleteById an iridologist from the database using the IridologistRepository
     *
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void deleteIridologist() {
        iridologistRepository.delete(johnHamcock);
        iridologistRepository.flush();

        Optional<Iridologist> iridologistToCheck = iridologistRepository.findById(johnHamcock.getId());
        assertTrue(iridologistToCheck.isEmpty());
    }

    /**
     * This test attempts to retrieve an Iridologist from the database through their username
     *
     * @throws AssertionError either if the Iridologist cannot be found in the database or if the fetched iridologist is
     * not the one with the username
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void getIridologistByUsername() {
        Iridologist found = iridologistRepository.findByUsername(janeDoe.getUsername())
                .orElseThrow(() -> new AssertionError("Iridologist not found in DB"));

        assertEquals(janeDoe, found);
    }

    /**
     * This test attempts to check whether an iridologist from the database with a given username exists
     *
     * @throws AssertionError if it does not find the existing iridologist
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void checkExistingIridologistExistenceByUsername() {
        assertTrue(iridologistRepository.existsByUsername(janeDoe.getUsername()));
    }

    /**
     * This test attempts to check whether an iridologist not in the database with a given username exists
     *
     * @throws AssertionError if it finds anything when it should find nothing
     * @see Iridologist
     * @see IridologistRepository
     */
    @Test
    void checkNonExistentIridologistExistenceByUsername() {
        assertFalse(iridologistRepository.existsByUsername("interestingUserName"));
    }
}
