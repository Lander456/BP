package com.example.bp.DALtests;

import com.example.bp.DAL.model.IrisImage;
import com.example.bp.DAL.model.Patient;
import com.example.bp.DAL.repository.IrisImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class IrisImageRepositoryTests {

    @Autowired
    private IrisImageRepository irisImageRepository;

    @Autowired
    private TestEntityManager entityManager;

    /// private Patient instance, predefined inside the setUp method for use in tests
    private Patient patientZero;

    /// private Patient instance, predefined inside the setUp method for use in tests
    private Patient patientOne;

    /// private IrisImage instance, predefined inside the setUp method for use in tests
    private IrisImage firstIrisImagePatientZero;

    /// private IrisImage instance, predefined inside the setUp method for use in tests
    private IrisImage secondIrisImagePatientZero;

    /// private IrisImage instance, predefined inside the setUp method for use in tests
    private IrisImage firstIrisImagePatientOne;

    /// private IrisImage instance, predefined inside the setUp method for use in tests
    private IrisImage secondIrisImagePatientOne;

    /// setup method executed before each test to ensure database is reset to this state and populated with certain data
    @BeforeEach
    void setUp() {
        patientZero = new Patient("Patient", "Zero", (byte) 69, "999999/99");
        patientOne = new Patient("Patient", "One", (byte) 50, "999999/99");
        firstIrisImagePatientZero = new IrisImage("irisImageUrl1");
        secondIrisImagePatientZero = new IrisImage("irisImageUrl2");
        firstIrisImagePatientOne = new IrisImage("irisImageUrl3");
        secondIrisImagePatientOne = new IrisImage("irisImageUrl4");

        firstIrisImagePatientZero.setPatient(patientZero);
        secondIrisImagePatientZero.setPatient(patientZero);
        firstIrisImagePatientOne.setPatient(patientOne);
        secondIrisImagePatientOne.setPatient(patientOne);

        entityManager.persist(patientZero);
        entityManager.persist(patientOne);
        entityManager.persist(firstIrisImagePatientZero);
        entityManager.persist(secondIrisImagePatientZero);
        entityManager.persist(firstIrisImagePatientOne);
        entityManager.persist(secondIrisImagePatientOne);

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * This test attempts to save a new iris image with a unique URL using the IrisImageRepository
     *
     * @see IrisImage
     * @see IrisImageRepository
     */
    @Test
    void saveUniqueIrisImage() {
        IrisImage irisImage = new IrisImage("newImageUrl");
        irisImage.setPatient(patientZero);

        assertDoesNotThrow(() -> irisImageRepository.saveAndFlush(irisImage));
    }

    /**
     * This test attempts to save an iris image with a duplicate URL using the IrisImageRepository
     *
     * @see IrisImage
     * @see IrisImageRepository
     */
    @Test
    void saveDuplicateImage() {
        IrisImage duplicateIrisImage = new IrisImage(firstIrisImagePatientZero.getImageUrl());
        duplicateIrisImage.setPatient(patientZero);

        assertThrows(DataIntegrityViolationException.class,() -> irisImageRepository.saveAndFlush(duplicateIrisImage));
    }

    /**
     * This test attempts to retrieve all the irisImages from the database using the IrisImageRepository it checks the integrity
     * of the IrisImages it got
     *
     * @see IrisImage
     * @see IrisImageRepository
     */
    @Test
    void getAllIrisImages() {
        List<IrisImage> irisImages = irisImageRepository.findAll();

        assertThat(irisImages).extracting(
                        img -> img.getPatient().getFirstName(),
                        img -> img.getPatient().getLastName(),
                        img -> img.getPatient().getAge(),
                        img -> img.getPatient().getBirthNum(),
                        IrisImage::getImageUrl
                )
                .containsExactlyInAnyOrder(
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), firstIrisImagePatientZero.getImageUrl()),
                        tuple(patientOne.getFirstName(), patientOne.getLastName(), patientOne.getAge(), patientOne.getBirthNum(), firstIrisImagePatientOne.getImageUrl()),
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), secondIrisImagePatientZero.getImageUrl()),
                        tuple(patientOne.getFirstName(), patientOne.getLastName(), patientOne.getAge(), patientOne.getBirthNum(), secondIrisImagePatientOne.getImageUrl())
                );
    }

    /**
     * This test attempts to retrieve an irisImage by ID using the IrisImageRepository and checks its integrity
     *
     * @see IrisImage
     * @see IrisImageRepository
     */
    @Test
    void getIrisImageById() {
        IrisImage irisImage = irisImageRepository.findById(firstIrisImagePatientZero.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisImage"));

        assertEquals(irisImage, firstIrisImagePatientZero);
    }

    /**
     * This test attempts to update an irisImage's URL in the database, fetching and saving the image using the
     * IrisImageRepository
     *
     * @see IrisImage
     * @see IrisImageRepository
     */
    @Test
    void updateIrisImageUrl() {
        IrisImage irisImage = irisImageRepository.findById(firstIrisImagePatientZero.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisImage"));
        irisImage.setImageUrl("newImageUrl");

        irisImageRepository.saveAndFlush(irisImage);

        IrisImage irisImageToCompare = irisImageRepository.findById(firstIrisImagePatientZero.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisImage"));
        assertEquals(irisImage.getImageUrl(), irisImageToCompare.getImageUrl());
    }

    /**
     * This test attempts to delete an irisImage from the database using the IrisImageRepository
     *
     * @see IrisImage
     * @see IrisImageRepository
     */
    @Test
    void deleteIrisImage() {
        irisImageRepository.delete(firstIrisImagePatientZero);
        irisImageRepository.flush();

        Optional<IrisImage> irisImage = irisImageRepository.findById(firstIrisImagePatientZero.getId());
        assertTrue(irisImage.isEmpty());
    }
}
