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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class IridologistRepositoryTests {

    @Autowired
    private IridologistRepository iridologistRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.persist(new Iridologist("John", "Doe"));
        entityManager.persist(new Iridologist("Jane", "Doe"));
        entityManager.persist(new Iridologist("John", "Hamcock"));
        entityManager.persist(new Patient("Patient", "Zero", (byte) 59, "999999/99"));
        entityManager.persist(new IrisMap("someImageUrl"));
    }

    @Test
    void saveIridologist() {
        Iridologist iridologist = new Iridologist("Jeffrey", "Doe");

        iridologistRepository.save(iridologist);

        List<Iridologist> found = iridologistRepository.findAll();
        assertThat(found).extracting(Iridologist::getFirstName, Iridologist::getLastName)
                .contains(tuple("Jeffrey", "Doe"));
    }

    @Test
    void findIridologistByFirstName() {
        List<Iridologist> found = iridologistRepository.findByFirstName("Jane");
        assertThat(found).extracting(Iridologist::getFirstName, Iridologist::getLastName)
                .containsExactly(tuple("Jane", "Doe"));
    }

    @Test
    void findMultipleIridologistsByFirstName() {
        Iridologist iridologist1 = new Iridologist("John", "Doe");
        Iridologist iridologist2 = new Iridologist("John", "Davies");
        Iridologist iridologist3 = new Iridologist("John", "Hamcock");

        iridologistRepository.saveAll(List.of(iridologist1, iridologist2, iridologist3));

        List<Iridologist> found = iridologistRepository.findByFirstName("John");
        assertThat(found).extracting(Iridologist::getFirstName, Iridologist::getLastName)
                .contains(
                        tuple("John", "Doe"),
                        tuple("John", "Davies"),
                        tuple("John", "Hamcock"));
    }

    @Test
    void saveAndFindById() {
        Iridologist iridologist = new Iridologist("Jane", "Doe");

        iridologistRepository.save(iridologist);

        Optional<Iridologist> found = iridologistRepository.findById(iridologist.getId());
        assertTrue(found.isPresent());
    }

    //@Test
    //void updateExistingFirstName() {

        //Optional<Iridologist> found = iridologistRepository.findById(iridologist.getId());
    //}
}
