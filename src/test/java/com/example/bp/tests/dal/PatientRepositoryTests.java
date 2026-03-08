package com.example.bp.tests.dal;

import com.example.bp.dal.entity.Iridologist;
import com.example.bp.dal.entity.Patient;
import com.example.bp.dal.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class PatientRepositoryTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PatientRepository patientRepository;

    /// predeclared Patient variable, initialized before each test via the setUp method
    private Patient patientZero;

    /// predeclared Patient variable, initialized before each test via the setUp method
    private Patient patientOne;

    /// predeclared Patient variable, initialized before each test via the setUp method
    private Iridologist iridologist;

    /**
     * setUp method ran before each test to ensure a certain database setup before tests on the database are ran
     */
    @BeforeEach
    void setUp() {
        patientZero = new Patient("Patient", "Zero", (byte) 80, "99999/99", true);
        patientOne = new Patient("Patient", "One", (byte) 33, "9999/999", false);
        iridologist = new Iridologist("Jane", "Doe", "janedoe", "interestingpass");

        patientZero.setIridologist(iridologist);
        patientOne.setIridologist(iridologist);

        entityManager.persist(iridologist);
        entityManager.persist(patientZero);
        entityManager.persist(patientOne);

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * This test attempts to create a new patient instance and save it in the database, it then fetches this instance
     * from the database and checks whether it is the same as the one it tried to save (it should)
     *
     * @throws AssertionError if the fetched instance is not the same as the saved instance
     * @throws EntityNotFoundException if the test fails to find the instance that has just been saved
     * @see Patient
     * @see PatientRepository
     */
    @Test
    void createNewPatient() {
        Patient newPatient = new Patient("New", "Patient", (byte) 70, "22222/22", true);
        newPatient.setIridologist(iridologist);

        Patient saved = patientRepository.saveAndFlush(newPatient);
        Patient found = patientRepository.findById(saved.getId())
                .orElseThrow(() -> new EntityNotFoundException("Did not find patient with id: " + saved.getId() + " in the database"));

        assertEquals(saved, found);
    }

    /**
     * This test attempts to retrieve a previously saved patient from the database
     *
     * @throws EntityNotFoundException if it does not find the patient in the database
     * @throws AssertionError if the fetched patient is not the same as the patient that was saved
     * @see Patient
     * @see PatientRepository
     */
    @Test
    void getPatientById() {
        Patient foundPatient = patientRepository.findById(patientZero.getId())
                .orElseThrow(() -> new EntityNotFoundException("Failed to find patient with id: " + patientZero.getId() + " in the database"));

        assertEquals(patientZero, foundPatient);
    }

    /**
     * This test attempts to fetch all patients with a certain first name from the database
     *
     * @throws AssertionError if the list of fetched patients does not contain the patients that have been saved previously
     * @see Patient
     * @see PatientRepository
     */
    @Test
    void getPatientsByFirstName() {
        List<Patient> foundPatients = patientRepository.findByFirstName(patientZero.getFirstName());

        assertThat(foundPatients).extracting(Patient::getFirstName, Patient::getLastName, Patient::getAge, Patient::getBirthNum, Patient::getIridologist, Patient::getSex)
                .containsExactlyInAnyOrder(
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), patientZero.getIridologist(), patientZero.getSex()),
                        tuple(patientOne.getFirstName(), patientOne.getLastName(), patientOne.getAge(), patientOne.getBirthNum(), patientOne.getIridologist(), patientOne.getSex())
                );
    }

    /**
     * This test attempts to fetch all patients with a certain last name from the database
     *
     * @throws AssertionError if the list of fetched patients does not contain the patients that have been saved previously
     * @see Patient
     * @see PatientRepository
     */
    @Test
    void getPatientsByLastName() {
        List<Patient> foundPatients = patientRepository.findByLastName(patientZero.getLastName());

        assertThat(foundPatients).extracting(Patient::getFirstName, Patient::getLastName, Patient::getAge, Patient::getBirthNum, Patient::getIridologist)
                .containsExactly(
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), patientZero.getIridologist())
                );
    }

    /**
     * This test attempts to fetch all patients with a certain age from the database
     *
     * @throws AssertionError if the fetched lsit does no contain the patients that were previously saved in the database
     * @see Patient
     * @see PatientRepository
     */
    @Test
    void getPatientsByAge() {
        List<Patient> foundPatients = patientRepository.findByAge(patientZero.getAge());

        assertThat(foundPatients).extracting(Patient::getFirstName, Patient::getLastName, Patient::getAge, Patient::getBirthNum, Patient::getIridologist)
                .containsExactly(
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), patientZero.getIridologist())
                );
    }

    /**
     * This test attempts to get all the patients with a certain birth number (should be one at most 90% of the time, however
     * nothing ensures absolute uniqueness of birth numbers)
     *
     * @throws AssertionError if the list of fetched patients does not contain the entities that have been previously saved
     * @see Patient
     * @see PatientRepository
     */
    @Test
    void getPatientsByBirthNum() {
        List<Patient> foundPatients = patientRepository.findByBirthNum(patientZero.getBirthNum());

        assertThat(foundPatients).extracting(Patient::getFirstName, Patient::getLastName, Patient::getAge, Patient::getBirthNum, Patient::getIridologist)
                .containsExactly(
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), patientZero.getIridologist())
                );
    }

    @Test
    void getPatientsBySex() {
        List<Patient> foundPatients = patientRepository.findBySex(true);

        assertThat(foundPatients).containsExactlyInAnyOrder(patientZero);
    }

    /**
     * This test attempts to fetch a patient from the database and update their first name, then save them back in the
     * database
     *
     * @throws EntityNotFoundException if it does not find the previously saved patient in the database
     * @throws AssertionError if the fetched entity is not the same as the updated one
     * @see Patient
     * @see PatientRepository
     */
    @Test
    void updatePatientsFirstName() {
        Patient patientToUpdate = patientRepository.findById(patientZero.getId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find patient with this id: " + patientZero.getId() + " in the database"));

        patientToUpdate.setFirstName("newFirstName");
        patientRepository.saveAndFlush(patientToUpdate);

        Patient controlPatient = patientRepository.findById(patientToUpdate.getId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find patient with this id: " + patientZero.getId() + " in the database"));
        assertEquals(patientToUpdate, controlPatient);
    }

    @Test
    void updatePatientsLastName() {
        Patient patientToUpdate = patientRepository.findById(patientZero.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch patient from DB"));

        patientToUpdate.setLastName("newLastName");
        patientRepository.saveAndFlush(patientToUpdate);

        Patient controlPatient = patientRepository.findById(patientToUpdate.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch patient from DB"));
        assertEquals(patientToUpdate, controlPatient);
    }

    @Test
    void updatePatientsAge() {
        Patient patientToUpdate = patientRepository.findById(patientZero.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch patient from DB"));

        patientToUpdate.setAge((byte) 99);
        patientRepository.saveAndFlush(patientToUpdate);

        Patient controlPatient = patientRepository.findById(patientToUpdate.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch patient from DB"));
        assertEquals(patientToUpdate, controlPatient);
    }

    @Test
    void updatePatientsBirthNum() {
        Patient patientToUpdate = patientRepository.findById(patientZero.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch patient from DB"));

        patientToUpdate.setBirthNum("newBirthNum");
        patientRepository.saveAndFlush(patientToUpdate);

        Patient controlPatient = patientRepository.findById(patientToUpdate.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch patient from DB"));
        assertEquals(patientToUpdate, controlPatient);
    }

    @Test
    void deleteExistingPatient() {
        patientRepository.delete(patientOne);

        Optional<Patient> deletedPatient = patientRepository.findById(patientOne.getId());

        assertTrue(deletedPatient.isEmpty());
    }
}
