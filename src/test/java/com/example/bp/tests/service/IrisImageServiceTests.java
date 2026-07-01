package com.example.bp.tests.service;

import com.example.bp.api.dto.IrisImageCreateDto;
import com.example.bp.api.dto.IrisImageDetailDto;
import com.example.bp.api.dto.IrisImageListDto;
import com.example.bp.api.mapper.IrisImageMapperImpl;
import com.example.bp.common.Sex;
import com.example.bp.dal.entity.IrisImage;
import com.example.bp.dal.entity.Patient;
import com.example.bp.dal.repository.IrisImageRepository;
import com.example.bp.dal.repository.PatientRepository;
import com.example.bp.service.impl.FileStorageServiceImpl;
import com.example.bp.service.impl.IrisImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IrisImageServiceTests {

    @InjectMocks
    IrisImageServiceImpl irisImageService;

    @Mock
    private FileStorageServiceImpl fileStorageService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private IrisImageMapperImpl irisImageMapper;

    @Mock
    private IrisImageRepository irisImageRepository;

    @Test
    void uploadIrisImageWithValidPatientExecutes() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "mockFile.txt",
                MediaType.IMAGE_JPEG_VALUE,
                "mockImage".getBytes()
        );
        IrisImageCreateDto createDto = new IrisImageCreateDto(1L, "LEFT", "mockNotes");
        Patient patient = new Patient("firstName", "lastName", (byte) 69, "birthNum", Sex.MALE);
        IrisImage irisImage = new IrisImage("mockImageUrl");
        IrisImageDetailDto detailDto = new IrisImageDetailDto(1L, irisImage.getImageUrl(), mockFile.getOriginalFilename(), mockFile.getContentType(), mockFile.getSize(), patient.getFirstName(), patient.getLastName(), patient.getAge(), patient.getSex());

        when(patientRepository.findById(createDto.patientId())).thenReturn(Optional.of(patient));
        when(irisImageRepository.save(any(IrisImage.class))).thenReturn(irisImage);
        when(irisImageMapper.toDetailDto(irisImage)).thenReturn(detailDto);
        when(fileStorageService.save(mockFile)).thenReturn("generatedUniqueName");

        irisImageService.upload(mockFile, createDto);

        verify(patientRepository).findById(createDto.patientId());
        verify(irisImageRepository).save(any(IrisImage.class));
        verify(irisImageMapper).toDetailDto(irisImage);

        verifyNoMoreInteractions(patientRepository);
        verifyNoMoreInteractions(irisImageRepository);
        verifyNoMoreInteractions(irisImageMapper);
        verifyNoMoreInteractions(fileStorageService);
    }

    @Test
    void uploadIrisImageWithInvalidPatientThrows() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "mockFile.txt",
                MediaType.IMAGE_JPEG_VALUE,
                "mockImage".getBytes()
        );
        IrisImageCreateDto dto = new IrisImageCreateDto(1L, "LEFT", "mockNotes");

        when(patientRepository.findById(dto.patientId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> irisImageService.upload(mockFile, dto));

        assertTrue(exception.getMessage().contains("Patient not found with ID: " + dto.patientId()));

        verify(patientRepository).findById(dto.patientId());

        verifyNoInteractions(irisImageRepository);
        verifyNoInteractions(fileStorageService);
        verifyNoInteractions(irisImageMapper);
    }

    @Test
    void findByValidImageIdExecutes() {
        Long id = 1L;
        IrisImage irisImage = new IrisImage("mockImageUrl");
        IrisImageDetailDto dto = new IrisImageDetailDto(1L, irisImage.getImageUrl(), "originalFilename", "contentType", 128, "firstName", "lastName", (byte) 69, Sex.MALE);

        when(irisImageRepository.findById(id)).thenReturn(Optional.of(irisImage));
        when(irisImageMapper.toDetailDto(irisImage)).thenReturn(dto);

        irisImageService.getById(id);

        verify(irisImageRepository).findById(id);
        verify(irisImageMapper).toDetailDto(irisImage);

        verifyNoMoreInteractions(irisImageRepository);
        verifyNoMoreInteractions(irisImageMapper);

        verifyNoInteractions(fileStorageService);
        verifyNoInteractions(patientRepository);
    }

    @Test
    void findByInvalidImageIdThrows() {
        Long id = 1L;

        when(irisImageRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> irisImageService.getById(id));

        assertTrue(exception.getMessage().contains("Iris image not found with ID: " + id));

        verify(irisImageRepository).findById(id);

        verifyNoMoreInteractions(irisImageRepository);

        verifyNoInteractions(irisImageMapper);
        verifyNoInteractions(patientRepository);
        verifyNoInteractions(fileStorageService);
    }

    @Test
    void findByValidPatientIdExecutes() {
        Long id = 1L;
        IrisImage irisImage = new IrisImage("mockImageUrl");
        IrisImageListDto dto = new IrisImageListDto(1L, "mockImageUrl", LocalDateTime.now(), "mockLabel");
        List<IrisImage> foundImages = List.of(irisImage);
        List<IrisImageListDto> listDtoList = List.of(dto);

        when(patientRepository.existsById(id)).thenReturn(true);
        when(irisImageRepository.findByPatient_Id(id)).thenReturn(foundImages);
        when(irisImageMapper.toListDtoList(foundImages)).thenReturn(listDtoList);

        irisImageService.getByPatientId(id);

        verify(patientRepository).existsById(id);
        verify(irisImageRepository).findByPatient_Id(id);
        verify(irisImageMapper).toListDtoList(foundImages);

        verifyNoMoreInteractions(patientRepository);
        verifyNoMoreInteractions(irisImageRepository);
        verifyNoMoreInteractions(irisImageMapper);

        verifyNoInteractions(fileStorageService);
    }

    @Test
    void findByInvalidPatientIdThrows() {
        Long id = 1L;

        when(patientRepository.existsById(id)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> irisImageService.getByPatientId(id));

        assertTrue(exception.getMessage().contains("Patient with ID: " + id + " not found."));

        verify(patientRepository).existsById(id);

        verifyNoMoreInteractions(patientRepository);

        verifyNoInteractions(irisImageRepository);
        verifyNoInteractions(irisImageMapper);
        verifyNoInteractions(fileStorageService);
    }
}
