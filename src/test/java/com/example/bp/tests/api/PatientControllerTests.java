package com.example.bp.tests.api;

import com.example.bp.api.controllers.PatientController;
import com.example.bp.api.dto.PatientCreateDto;
import com.example.bp.api.dto.PatientDetailDto;
import com.example.bp.api.dto.PatientListDto;
import com.example.bp.api.dto.PatientUpdateDto;
import com.example.bp.common.Sex;
import com.example.bp.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @Test
    void createShouldReturn200WhenSuccessful() throws Exception {
        PatientDetailDto detailDto = new PatientDetailDto(
                1L,
                "John",
                "Doe",
                (byte) 30,
                "123456789",
                Sex.MALE
        );

        when(patientService.create(any(PatientCreateDto.class))).thenReturn(detailDto);

        mockMvc.perform(post("/patient/create")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("age", "30")
                        .param("birthNum", "123456789")
                        .param("sex", "MALE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.birthNum").value("123456789"))
                .andExpect(jsonPath("$.sex").value("MALE"));
    }

    @Test
    void getByIdShouldReturn200WhenFound() throws Exception {
        PatientDetailDto detailDto = new PatientDetailDto(
                1L,
                "John",
                "Doe",
                (byte) 30,
                "123456789",
                Sex.MALE
        );
        when(patientService.getById(1L)).thenReturn(detailDto);

        mockMvc.perform(get("/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.birthNum").value("123456789"))
                .andExpect(jsonPath("$.sex").value("MALE"));
    }

    @Test
    void getByIdShouldReturn404WhenNotFound() throws Exception {
        when(patientService.getById(1L))
                .thenThrow(new EntityNotFoundException("Patient not found"));

        mockMvc.perform(get("/patient/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }

    @Test
    void getAllShouldReturn200() throws Exception {
        List<PatientListDto> listDtos = List.of(
                new PatientListDto(1L, (byte) 30, "John", "Doe"),
                new PatientListDto(2L, (byte) 25, "Jane", "Smith")
        );
        when(patientService.getAll()).thenReturn(listDtos);

        mockMvc.perform(get("/patient/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].firstName").value("John"))
                .andExpect(jsonPath("$.[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.[0].age").value(30))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].firstName").value("Jane"))
                .andExpect(jsonPath("$.[1].lastName").value("Smith"))
                .andExpect(jsonPath("$.[1].age").value(25));
    }

    @Test
    void getByFirstNameShouldReturn200() throws Exception {
        List<PatientListDto> listDtos = List.of(
                new PatientListDto(1L, (byte) 30, "John", "Doe")
        );
        when(patientService.getByFirstName("John")).thenReturn(listDtos);

        mockMvc.perform(get("/patient/filterFirstName/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].firstName").value("John"))
                .andExpect(jsonPath("$.[0].lastName").value("Doe"));
    }

    @Test
    void getByFirstNameShouldReturn404WhenNotFound() throws Exception {
        when(patientService.getByFirstName("NonExistent"))
                .thenThrow(new EntityNotFoundException("Patient not found"));

        mockMvc.perform(get("/patient/filterFirstName/NonExistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }

    @Test
    void getByLastNameShouldReturn200() throws Exception {
        List<PatientListDto> listDtos = List.of(
                new PatientListDto(1L, (byte) 30, "John", "Doe")
        );
        when(patientService.getByLastName("Doe")).thenReturn(listDtos);

        mockMvc.perform(get("/patient/filterLastName/Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].firstName").value("John"))
                .andExpect(jsonPath("$.[0].lastName").value("Doe"));
    }

    @Test
    void getByLastNameShouldReturn404WhenNotFound() throws Exception {
        when(patientService.getByLastName("NonExistent"))
                .thenThrow(new EntityNotFoundException("Patient not found"));

        mockMvc.perform(get("/patient/filterLastName/NonExistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }

    @Test
    void updateShouldReturn204WhenSuccessful() throws Exception {
        doNothing().when(patientService).update(eq(1L), any(PatientUpdateDto.class));

        mockMvc.perform(patch("/patient/update/1")
                        .param("firstName", "JohnUpdated")
                        .param("lastName", "DoeUpdated"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateShouldReturn404WhenNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Patient not found"))
                .when(patientService).update(eq(99L), any(PatientUpdateDto.class));

        mockMvc.perform(patch("/patient/update/99")
                        .param("firstName", "JohnUpdated"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }

    @Test
    void deleteShouldReturn204WhenSuccessful() throws Exception {
        doNothing().when(patientService).delete(1L);

        mockMvc.perform(delete("/patient/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404WhenNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Patient not found"))
                .when(patientService).delete(1L);

        mockMvc.perform(delete("/patient/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }
}
