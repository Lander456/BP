package com.example.bp.tests.dal;

import com.example.bp.dal.entity.IrisImage;
import com.example.bp.dal.entity.Patient;
import com.example.bp.dal.repository.IrisImageRepository;
import net.bytebuddy.asm.Advice;
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

import java.time.LocalDateTime;
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
    private IrisImage image1PatientZero;

    /// private IrisImage instance, predefined inside the setUp method for use in tests
    private IrisImage image2PatientZero;

    /// private IrisImage instance, predefined inside the setUp method for use in tests
    private IrisImage image1PatientOne;

    /// private IrisImage instance, predefined inside the setUp method for use in tests
    private IrisImage image2PatientOne;

    /// setup method executed before each test to ensure database is reset to this state and populated with certain data
    @BeforeEach
    void setUp() {
        patientZero = new Patient("Patient", "Zero", (byte) 69, "999999/99", true);
        patientOne = new Patient("Patient", "One", (byte) 50, "999999/99", false);
        image1PatientZero = new IrisImage("irisImageUrl1");
        image2PatientZero = new IrisImage("irisImageUrl2");
        image1PatientOne = new IrisImage("irisImageUrl3");
        image2PatientOne = new IrisImage("irisImageUrl4");

        image1PatientZero.setUploadedAt(LocalDateTime.now());
        image2PatientZero.setUploadedAt(LocalDateTime.now());
        image1PatientOne.setUploadedAt(LocalDateTime.now());
        image2PatientOne.setUploadedAt(LocalDateTime.now());

        image1PatientZero.setPatient(patientZero);
        image2PatientZero.setPatient(patientZero);
        image1PatientOne.setPatient(patientOne);
        image2PatientOne.setPatient(patientOne);

        image1PatientZero.setStoragePath("firstImageFirstPatientStorage");
        image2PatientZero.setStoragePath("secondImageFirstPatientStorage");
        image1PatientOne.setStoragePath("firstImageSecondPatientStorage");
        image2PatientOne.setStoragePath("secondImageSecondPatientStorage");

        entityManager.persist(patientZero);
        entityManager.persist(patientOne);
        entityManager.persist(image1PatientZero);
        entityManager.persist(image2PatientZero);
        entityManager.persist(image1PatientOne);
        entityManager.persist(image2PatientOne);

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

        irisImage.setUploadedAt(LocalDateTime.now());
        irisImage.setStoragePath("newStorage");
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
        IrisImage duplicateIrisImage = new IrisImage(image1PatientZero.getImageUrl());
        duplicateIrisImage.setPatient(patientZero);

        assertThrows(DataIntegrityViolationException.class, () -> irisImageRepository.saveAndFlush(duplicateIrisImage));
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
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), image1PatientZero.getImageUrl()),
                        tuple(patientOne.getFirstName(), patientOne.getLastName(), patientOne.getAge(), patientOne.getBirthNum(), image1PatientOne.getImageUrl()),
                        tuple(patientZero.getFirstName(), patientZero.getLastName(), patientZero.getAge(), patientZero.getBirthNum(), image2PatientZero.getImageUrl()),
                        tuple(patientOne.getFirstName(), patientOne.getLastName(), patientOne.getAge(), patientOne.getBirthNum(), image2PatientOne.getImageUrl())
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
        IrisImage irisImage = irisImageRepository.findById(image1PatientZero.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisImage"));

        assertEquals(irisImage, image1PatientZero);
    }

    @Test
    void getIrisImagesUploadedAfterYesterday() {
        List<IrisImage> irisImages = irisImageRepository.findByUploadedAtAfter(LocalDateTime.now().minusDays(1));

        assertThat(irisImages).containsExactlyInAnyOrder(image1PatientZero, image2PatientZero, image1PatientOne, image2PatientOne);
    }

    @Test
    void getIrisImagesUploadedAfterTomorrow() {
        List<IrisImage> irisImages = irisImageRepository.findByUploadedAtAfter(LocalDateTime.now().plusDays(1));

        assertTrue(irisImages.isEmpty());
    }

    @Test
    void getIrisImagesUploadedBeforeYesterday() {
        List<IrisImage> irisImages = irisImageRepository.findByUploadedAtBefore(LocalDateTime.now().minusDays(1));

        assertTrue(irisImages.isEmpty());
    }

    @Test
    void getIrisImagesUploadedBeforeTomorrow() {
        List<IrisImage> irisImages = irisImageRepository.findByUploadedAtBefore(LocalDateTime.now().plusDays(1));

        assertThat(irisImages).contains(image1PatientZero, image2PatientZero, image1PatientOne, image2PatientOne);
    }

    @Test
    void getIrisImagesUploadedBetweenYesterdayAndTomorrow() {
        List<IrisImage> irisImages = irisImageRepository.findByUploadedAtBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));

        assertThat(irisImages).contains(image1PatientZero, image2PatientZero, image1PatientOne, image2PatientOne);
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
        IrisImage irisImage = irisImageRepository.findById(image1PatientZero.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisImage"));
        irisImage.setImageUrl("newImageUrl");

        irisImageRepository.saveAndFlush(irisImage);

        IrisImage irisImageToCompare = irisImageRepository.findById(image1PatientZero.getId())
                .orElseThrow(() -> new AssertionError("Failed to fetch irisImage"));
        assertEquals(irisImage.getImageUrl(), irisImageToCompare.getImageUrl());
    }

    /**
     * This test attempts to deleteById an irisImage from the database using the IrisImageRepository
     *
     * @see IrisImage
     * @see IrisImageRepository
     */
    @Test
    void deleteIrisImage() {
        irisImageRepository.delete(image1PatientZero);
        irisImageRepository.flush();

        Optional<IrisImage> irisImage = irisImageRepository.findById(image1PatientZero.getId());
        assertTrue(irisImage.isEmpty());
    }
}
