package com.example.bp.tests.api;

import com.example.bp.api.controllers.IrisMapController;
import com.example.bp.api.dto.IrisMapCreateDto;
import com.example.bp.api.dto.IrisMapDetailDto;
import com.example.bp.api.dto.IrisMapListDto;
import com.example.bp.api.dto.IrisMapUpdateDto;
import com.example.bp.api.dto.IrisSectorDetailDto;
import com.example.bp.service.IrisMapService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IrisMapController.class)
public class IrisMapControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IrisMapService irisMapService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getByIdShouldReturn200WhenFound() throws Exception {
        IrisMapDetailDto detailDto = new IrisMapDetailDto(
                1L,
                "http://storage/map.jpg",
                1L,
                List.of(new IrisSectorDetailDto(1L, "Sector1", 0.0, 1.0, 0.0, 1.0, "Desc", 1L))
        );
        when(irisMapService.getById(1L)).thenReturn(detailDto);

        mockMvc.perform(get("/map/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.imageUrl").value("http://storage/map.jpg"))
                .andExpect(jsonPath("$.iridologistId").value(1L))
                .andExpect(jsonPath("$.sectors[0].id").value(1L))
                .andExpect(jsonPath("$.sectors[0].name").value("Sector1"));
    }

    @Test
    void getByIdShouldReturn404WhenNotFound() throws Exception {
        when(irisMapService.getById(1L))
                .thenThrow(new EntityNotFoundException("Iris map not found"));

        mockMvc.perform(get("/map/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris map not found"));
    }

    @Test
    void uploadShouldReturn200WhenSuccessful() throws Exception {
        IrisMapCreateDto createDto = new IrisMapCreateDto("http://storage/map.jpg", 1L, List.of());
        IrisMapDetailDto detailDto = new IrisMapDetailDto(1L, "http://storage/map.jpg", 1L, List.of());

        when(irisMapService.create(eq(createDto), any(MultipartFile.class))).thenReturn(detailDto);

        MockMultipartFile file = new MockMultipartFile("file", "map.jpg", "image/jpeg", "mapBytes".getBytes());
        MockMultipartFile imageData = new MockMultipartFile(
                "imageData",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createDto)
        );

        mockMvc.perform(multipart("/map/upload")
                        .file(file)
                        .file(imageData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.imageUrl").value("http://storage/map.jpg"))
                .andExpect(jsonPath("$.iridologistId").value(1L));
    }

    @Test
    void uploadShouldReturn404WhenIridologistNotFound() throws Exception {
        IrisMapCreateDto createDto = new IrisMapCreateDto("http://storage/map.jpg", 99L, List.of());

        when(irisMapService.create(any(IrisMapCreateDto.class), any(MultipartFile.class)))
                .thenThrow(new EntityNotFoundException("Iridologist not found"));

        MockMultipartFile file = new MockMultipartFile("file", "map.jpg", "image/jpeg", "mapBytes".getBytes());
        MockMultipartFile imageData = new MockMultipartFile(
                "imageData",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createDto)
        );

        mockMvc.perform(multipart("/map/upload")
                        .file(file)
                        .file(imageData))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iridologist not found"));
    }

    @Test
    void updateShouldReturn200WhenSuccessful() throws Exception {
        IrisMapUpdateDto updateDto = new IrisMapUpdateDto(1L, "http://storage/updated.jpg", List.of());
        IrisMapDetailDto detailDto = new IrisMapDetailDto(1L, "http://storage/updated.jpg", 1L, List.of());

        when(irisMapService.update(updateDto)).thenReturn(detailDto);

        mockMvc.perform(patch("/map/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.imageUrl").value("http://storage/updated.jpg"));
    }

    @Test
    void updateShouldReturn400WhenNoBody() throws Exception {
        mockMvc.perform(patch("/map/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateShouldReturn404WhenNotFound() throws Exception {
        IrisMapUpdateDto updateDto = new IrisMapUpdateDto(99L, "http://storage/updated.jpg", List.of());

        when(irisMapService.update(any(IrisMapUpdateDto.class)))
                .thenThrow(new EntityNotFoundException("Iris map not found"));

        mockMvc.perform(patch("/map/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris map not found"));
    }

    @Test
    void getByIridologistIdShouldReturn200() throws Exception {
        List<IrisMapListDto> listDtos = List.of(
                new IrisMapListDto(1L, "http://storage/map1.jpg", 1L, 5),
                new IrisMapListDto(2L, "http://storage/map2.jpg", 1L, 3)
        );
        when(irisMapService.getByIridologistId(1L)).thenReturn(listDtos);

        mockMvc.perform(get("/map/iridologist/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].imageUrl").value("http://storage/map1.jpg"))
                .andExpect(jsonPath("$.[0].iridologistId").value(1L))
                .andExpect(jsonPath("$.[0].sectorCount").value(5))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].sectorCount").value(3));
    }

    @Test
    void getByIridologistIdShouldReturn404WhenNotFound() throws Exception {
        when(irisMapService.getByIridologistId(99L))
                .thenThrow(new EntityNotFoundException("Iridologist not found"));

        mockMvc.perform(get("/map/iridologist/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iridologist not found"));
    }

    @Test
    void getAllShouldReturn200() throws Exception {
        List<IrisMapListDto> listDtos = List.of(
                new IrisMapListDto(1L, "http://storage/map1.jpg", 1L, 5),
                new IrisMapListDto(2L, "http://storage/map2.jpg", 2L, 3)
        );
        when(irisMapService.getAll()).thenReturn(listDtos);

        mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[1].id").value(2L));
    }

    @Test
    void deleteShouldReturn204WhenSuccessful() throws Exception {
        doNothing().when(irisMapService).delete(1L);

        mockMvc.perform(delete("/map/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404WhenNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Iris map not found"))
                .when(irisMapService).delete(1L);

        mockMvc.perform(delete("/map/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris map not found"));
    }
}
