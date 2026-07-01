package com.example.bp.tests.service;

import com.example.bp.api.dto.PatientCreateDto;
import com.example.bp.api.dto.PatientDetailDto;
import com.example.bp.api.dto.PatientListDto;
import com.example.bp.api.dto.PatientUpdateDto;
import com.example.bp.api.mapper.PatientMapper;
import com.example.bp.common.Sex;
import com.example.bp.dal.entity.Patient;
import com.example.bp.dal.repository.PatientRepository;
import com.example.bp.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTests {

    @InjectMocks
    PatientServiceImpl patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @Test
    void createSucceeds() {
        PatientCreateDto createDto = new PatientCreateDto("firstName", "lastName", (byte) 16, "00000000", Sex.MALE);
        Patient patient = new Patient(createDto.firstName(), createDto.lastName(), createDto.age(), createDto.birthNum(), createDto.sex());
        PatientDetailDto detailDto = new PatientDetailDto(1L, createDto.firstName(), createDto.lastName(), createDto.age(), createDto.birthNum(), createDto.sex());

        when(patientMapper.toEntity(createDto)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toDetailDto(patient)).thenReturn(detailDto);

        patientService.create(createDto);

        verify(patientMapper).toEntity(createDto);
        verify(patientRepository).save(patient);
        verify(patientMapper).toDetailDto(patient);

        verifyNoMoreInteractions(patientMapper, patientRepository);
    }

    @Test
    void getByIdWithValidIdSucceeds() {
        Long id = 1L;
        Patient patient = new Patient("firstName", "lastName", (byte) 69, "000000000", Sex.MALE);
        PatientDetailDto dto = new PatientDetailDto(id, patient.getFirstName(), patient.getLastName(), patient.getAge(), patient.getBirthNum(), patient.getSex());

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientMapper.toDetailDto(patient)).thenReturn(dto);

        patientService.getById(id);

        verify(patientRepository).findById(id);
        verify(patientMapper).toDetailDto(patient);

        verifyNoMoreInteractions(patientMapper, patientRepository);
    }

    @Test
    void getByIdWithInvalidIdThrows() {
        Long id = 1L;

        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> patientService.getById(id));

        assertTrue(exception.getMessage().contains("Patient not found with ID: " + id));

        verify(patientRepository).findById(id);

        verifyNoMoreInteractions(patientRepository);

        verifyNoInteractions(patientMapper);
    }

    @Test
    void getAllSucceeds() {
        Patient patient = new Patient("firstName", "lastName", (byte) 68, "00000000", Sex.MALE);
        List<Patient> patients = List.of(patient);
        PatientListDto dto = new PatientListDto(1L, patient.getAge(), patient.getFirstName(), patient.getLastName());
        List<PatientListDto> dtoList = List.of(dto);

        when(patientRepository.findAll()).thenReturn(patients);
        when(patientMapper.toListDtoList(patients)).thenReturn(dtoList);

        patientService.getAll();

        verify(patientRepository).findAll();
        verify(patientMapper).toListDtoList(patients);

        verifyNoMoreInteractions(patientMapper, patientRepository);
    }

    @Test
    void getByFirstNameSucceeds() {
        String firstName = "firstName";
        Patient patient = new Patient(firstName, "lastName", (byte) 67, "0000000000", Sex.FEMALE);
        List<Patient> patientList = List.of(patient);
        PatientListDto dto = new PatientListDto(1L, patient.getAge(), patient.getFirstName(), patient.getLastName());
        List<PatientListDto> dtoList = List.of(dto);

        when(patientRepository.findByFirstName(firstName)).thenReturn(patientList);
        when(patientMapper.toListDtoList(patientList)).thenReturn(dtoList);

        patientService.getByFirstName(firstName);

        verify(patientRepository).findByFirstName(firstName);
        verify(patientMapper).toListDtoList(patientList);

        verifyNoMoreInteractions(patientMapper, patientRepository);
    }

    @Test
    void getByLastNameSucceeds() {
        String lastName = "lastName";
        Patient patient = new Patient("firstName", lastName, (byte) 67, "0000000000", Sex.FEMALE);
        List<Patient> patientList = List.of(patient);
        PatientListDto dto = new PatientListDto(1L, patient.getAge(), patient.getFirstName(), patient.getLastName());
        List<PatientListDto> dtoList = List.of(dto);

        when(patientRepository.findByLastName(lastName)).thenReturn(patientList);
        when(patientMapper.toListDtoList(patientList)).thenReturn(dtoList);

        patientService.getByLastName(lastName);

        verify(patientRepository).findByLastName(lastName);
        verify(patientMapper).toListDtoList(patientList);

        verifyNoMoreInteractions(patientMapper, patientRepository);
    }

    @Test
    void updateSucceeds() {
        Long id = 1L;
        String firstName = "newFirstName";
        String lastName = "newLastName";
        String birthNum = "000000011";
        Sex sex = Sex.FEMALE;
        PatientUpdateDto dto = new PatientUpdateDto(Optional.of(2L), Optional.of(firstName), Optional.of(lastName), Optional.of(sex), Optional.of(birthNum));
        Patient patient = new Patient(firstName, lastName, (byte) 66, birthNum, sex);

        when(patientMapper.toEntity(dto)).thenReturn(patient);

        patientService.update(id, dto);

        verify(patientMapper).toEntity(dto);
        verify(patientRepository).save(patient);

        verifyNoMoreInteractions(patientMapper, patientRepository);
    }
}
