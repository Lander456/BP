package com.example.bp.DALtests;

import com.example.bp.DAL.model.Iridologist;
import com.example.bp.DAL.model.IrisMap;
import com.example.bp.DAL.model.Patient;
import com.example.bp.DAL.repository.IridologistRepository;
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

    /// private Iridologist instance, predefined for ease of use in the tests
    private final Iridologist janeDoe = new Iridologist("Jane", "Doe");

    /// private Iridologist instance, predefined for ease of use in the tests
    private final Iridologist johnDoe = new Iridologist("John", "Doe");

    /// private Iridologist instance, predefined for ease of use in the tests
    private final Iridologist johnHamcock = new Iridologist("John", "Hamcock");

    /// private Patient instance, predefined for ease of use in the tests
    private final Patient patientZero = new Patient("Patient", "Zero", (byte) 59, "999999/99");

    /// private IrisMap instance, predefined for ease of use in the tests
    private final IrisMap irisMap = new IrisMap("someImageUrl");

    /// setup method executed before each test to ensure database is reset to this state and populated with certain data
    @BeforeEach
    void setUp() {
        patientZero.setIridologist(johnDoe);
        irisMap.setIridologist(johnDoe);

        entityManager.persist(johnDoe);
        entityManager.persist(janeDoe);
        entityManager.persist(johnHamcock);
        entityManager.persist(patientZero);
        entityManager.persist(irisMap);
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
        Iridologist iridologist = new Iridologist("Jeffrey", "Doe");

        iridologistRepository.save(iridologist);
        entityManager.flush();

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
        Iridologist iridologist = new Iridologist("irrelevant", "man");
        Iridologist saved = iridologistRepository.save(iridologist);
        entityManager.flush();

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
        iridologistRepository.save(iridologist);
        entityManager.flush();

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
        iridologistRepository.save(iridologist);

        entityManager.flush();

        List<Iridologist> foundToCompare = iridologistRepository.findByLastName("interestingLastName");

        assertEquals(foundToCompare.getFirst(), iridologist);
    }
}
