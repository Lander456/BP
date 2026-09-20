package com.example.bp.tests.api;

import com.example.bp.api.controllers.IrisImageController;
import com.example.bp.api.dto.IrisImageCreateDto;
import com.example.bp.api.dto.IrisImageDetailDto;
import com.example.bp.api.dto.IrisImageListDto;
import com.example.bp.common.Sex;
import com.example.bp.service.IrisImageService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IrisImageController.class)
public class IrisImageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IrisImageService irisImageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void uploadNewImageShouldReturn200WhenSuccessful() throws Exception {
        IrisImageCreateDto createDto = new IrisImageCreateDto(1L, "LEFT", "Test notes");
        IrisImageDetailDto detailDto = new IrisImageDetailDto(
                1L,
                "http://storage/image.jpg",
                "image.jpg",
                "image/jpeg",
                1024L,
                "John",
                "Doe",
                (byte) 35,
                Sex.MALE
        );

        when(irisImageService.upload(any(MultipartFile.class), eq(createDto))).thenReturn(detailDto);

        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "imageContent".getBytes());
        MockMultipartFile imageData = new MockMultipartFile(
                "imageData",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createDto)
        );

        mockMvc.perform(multipart("/image/upload")
                        .file(file)
                        .file(imageData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.imageUrl").value("http://storage/image.jpg"))
                .andExpect(jsonPath("$.originalFileName").value("image.jpg"))
                .andExpect(jsonPath("$.contentType").value("image/jpeg"))
                .andExpect(jsonPath("$.fileSize").value(1024))
                .andExpect(jsonPath("$.patientFirstName").value("John"))
                .andExpect(jsonPath("$.patientLastName").value("Doe"))
                .andExpect(jsonPath("$.patientAge").value(35))
                .andExpect(jsonPath("$.patientSex").value("MALE"));
    }

    @Test
    void uploadNewImageShouldReturn404WhenPatientNotFound() throws Exception {
        IrisImageCreateDto createDto = new IrisImageCreateDto(99L, "LEFT", "Test notes");
        when(irisImageService.upload(any(MultipartFile.class), any(IrisImageCreateDto.class)))
                .thenThrow(new EntityNotFoundException("Patient not found"));

        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "imageContent".getBytes());
        MockMultipartFile imageData = new MockMultipartFile(
                "imageData",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createDto)
        );

        mockMvc.perform(multipart("/image/upload")
                        .file(file)
                        .file(imageData))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }

    @Test
    void getByIdShouldReturn200WhenFound() throws Exception {
        IrisImageDetailDto detailDto = new IrisImageDetailDto(
                1L,
                "http://storage/image.jpg",
                "image.jpg",
                "image/jpeg",
                1024L,
                "John",
                "Doe",
                (byte) 35,
                Sex.MALE
        );
        when(irisImageService.getById(1L)).thenReturn(detailDto);

        mockMvc.perform(get("/image/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.imageUrl").value("http://storage/image.jpg"))
                .andExpect(jsonPath("$.patientFirstName").value("John"));
    }

    @Test
    void getByIdShouldReturn404WhenNotFound() throws Exception {
        when(irisImageService.getById(1L))
                .thenThrow(new EntityNotFoundException("Iris image not found"));

        mockMvc.perform(get("/image/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris image not found"));
    }

    @Test
    void getByPatientIdShouldReturn200() throws Exception {
        List<IrisImageListDto> listDtos = List.of(
                new IrisImageListDto(1L, "http://storage/image1.jpg", LocalDateTime.now(), "label1"),
                new IrisImageListDto(2L, "http://storage/image2.jpg", LocalDateTime.now(), "label2")
        );
        when(irisImageService.getByPatientId(1L)).thenReturn(listDtos);

        mockMvc.perform(get("/image/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].imageUrl").value("http://storage/image1.jpg"))
                .andExpect(jsonPath("$.[0].label").value("label1"))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].imageUrl").value("http://storage/image2.jpg"));
    }

    @Test
    void getByPatientIdShouldReturn404WhenPatientNotFound() throws Exception {
        when(irisImageService.getByPatientId(99L))
                .thenThrow(new EntityNotFoundException("Patient not found"));

        mockMvc.perform(get("/image/patient/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }

    @Test
    void deleteShouldReturn204WhenSuccessful() throws Exception {
        doNothing().when(irisImageService).delete(1L);

        mockMvc.perform(delete("/image/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404WhenNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Iris image not found"))
                .when(irisImageService).delete(1L);

        mockMvc.perform(delete("/image/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris image not found"));
    }
}
