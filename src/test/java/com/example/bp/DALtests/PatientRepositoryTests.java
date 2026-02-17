package com.example.bp.DALtests;

import com.example.bp.DAL.model.Iridologist;
import com.example.bp.DAL.model.Patient;
import com.example.bp.DAL.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class PatientRepositoryTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PatientRepository patientRepository;

    private Patient patientZero;

    private Patient patientOne;

    private Iridologist iridologist;

    @BeforeEach
    void setUp() {
        patientZero = new Patient("Patient", "Zero", (byte) 80, "99999/99");
        patientOne = new Patient("Patient", "One", (byte) 33, "9999/999");
        iridologist = new Iridologist("Jane", "Doe");

        patientZero.setIridologist(iridologist);
        patientOne.setIridologist(iridologist);

        entityManager.persist(iridologist);
        entityManager.persist(patientZero);
        entityManager.persist(patientOne);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void createNewPatient() {
        Patient newPatient = new Patient("New", "Patient", (byte) 70, "22222/22");
        newPatient.setIridologist(iridologist);

        patientRepository.saveAndFlush(newPatient);
        List<Patient> allPatients = patientRepository.findAll();

        assertThat(allPatients).extracting(Patient::getFirstName, Patient::getLastName, Patient::getAge, Patient::getBirthNum, Patient::getIridologist)
                .contains(
                        tuple(newPatient.getFirstName(), newPatient.getLastName(), newPatient.getAge(), newPatient.getBirthNum(), newPatient.getIridologist())
                );
    }

    @Test
    void getPatientById() {
        Patient foundPatient = patientRepository.findById(patientZero.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch patient from DB"));

        assertEquals(patientZero, foundPatient);
    }

    @Test
    void getPatientsByFirstName() {
        List<Patient> foundPatients = patientRepository.findByFirstName(patientZero.getFirstName());

        assertThat(foundPatients).extracting(Patient::getFirstName, Patient::getLastName, Patient::getAge, Patient::getBirthNum, Patient::getIridologist)
                .containsExactlyInAnyOrder(
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), patientZero.getIridologist()),
                        tuple(patientOne.getFirstName(), patientOne.getLastName(), patientOne.getAge(), patientOne.getBirthNum(), patientOne.getIridologist())
                );
    }

    @Test
    void getPatientsByLastName() {
        List<Patient> foundPatients = patientRepository.findByLastName(patientZero.getLastName());

        assertThat(foundPatients).extracting(Patient::getFirstName, Patient::getLastName, Patient::getAge, Patient::getBirthNum, Patient::getIridologist)
                .containsExactly(
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), patientZero.getIridologist())
                );
    }

    @Test
    void getPatientsByAge() {
        List<Patient> foundPatients = patientRepository.findByAge(patientZero.getAge());

        assertThat(foundPatients).extracting(Patient::getFirstName, Patient::getLastName, Patient::getAge, Patient::getBirthNum, Patient::getIridologist)
                .containsExactly(
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), patientZero.getIridologist())
                );
    }

    @Test
    void getPatientsByBirthNum() {
        List<Patient> foundPatients = patientRepository.findByBirthNum(patientZero.getBirthNum());

        assertThat(foundPatients).extracting(Patient::getFirstName, Patient::getLastName, Patient::getAge, Patient::getBirthNum, Patient::getIridologist)
                .containsExactly(
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), patientZero.getIridologist())
                );
    }
}
