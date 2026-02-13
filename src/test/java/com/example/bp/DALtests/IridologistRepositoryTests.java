package com.example.bp.DALtests;

import com.example.bp.DAL.model.Iridologist;
import com.example.bp.DAL.repository.IridologistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@DataJpaTest
@ActiveProfiles("test")
public class IridologistRepositoryTests {

    @Autowired
    private IridologistRepository iridologistRepository;

    @Test
    void saveIridologist() {
        Iridologist iridologist = new Iridologist("John", "Doe");

        iridologistRepository.save(iridologist);

        List<Iridologist> found = iridologistRepository.findAll();
        assertThat(found).extracting(Iridologist::getFirstName, Iridologist::getLastName)
                .contains(tuple("John", "Doe"));
    }

    @Test
    void saveAndFindIridologistByFirstName() {
        Iridologist iridologist = new Iridologist("John", "Doe");

        iridologistRepository.save(iridologist);

        List<Iridologist> found = iridologistRepository.findByFirstName("John");
        assertThat(found).extracting(Iridologist::getFirstName, Iridologist::getLastName)
                .contains(tuple("John", "Doe"));
    }

    @Test
    void saveAndFindMultipleIridologistsByFirstName() {
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
}
