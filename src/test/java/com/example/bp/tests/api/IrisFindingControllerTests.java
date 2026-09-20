package com.example.bp.tests.api;

import com.example.bp.api.controllers.IrisFindingController;
import com.example.bp.api.dto.ArtifactDetailDto;
import com.example.bp.api.dto.IrisFindingCreateDto;
import com.example.bp.api.dto.IrisFindingDetailDto;
import com.example.bp.api.dto.IrisFindingListDto;
import com.example.bp.api.dto.IrisFindingUpdateDto;
import com.example.bp.api.dto.IrisSectorDetailDto;
import com.example.bp.service.IrisFindingService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IrisFindingController.class)
public class IrisFindingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IrisFindingService irisFindingService;

    @Autowired
    private ObjectMapper objectMapper;

    private IrisFindingDetailDto createSampleDetailDto(Long id) {
        return new IrisFindingDetailDto(
                id,
                "imageId",
                new IrisSectorDetailDto(
                        1L,
                        "irisSectorName",
                        0.0,
                        1.0,
                        0.0,
                        1.0,
                        "irisSectorDescription",
                        1L
                ),
                new ArtifactDetailDto(
                        1L,
                        "artifactName",
                        "artifactDescription"
                ),
                "geometryJson",
                0.66,
                false,
                false
        );
    }

    @Test
    void createShouldReturn200WhenSuccessful() throws Exception {
        IrisFindingCreateDto createDto = new IrisFindingCreateDto(
                1L,
                1L,
                1L,
                0.66,
                "geometryJson"
        );
        IrisFindingDetailDto detailDto = createSampleDetailDto(1L);

        when(irisFindingService.create(createDto)).thenReturn(detailDto);

        mockMvc.perform(post("/api/irisFinding/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.irisImageImageId").value("imageId"))
                .andExpect(jsonPath("$.sector.id").value(1L))
                .andExpect(jsonPath("$.sector.name").value("irisSectorName"))
                .andExpect(jsonPath("$.sector.startAngle").value(0.0))
                .andExpect(jsonPath("$.sector.endAngle").value(1.0))
                .andExpect(jsonPath("$.sector.innerRadius").value(0.0))
                .andExpect(jsonPath("$.sector.outerRadius").value(1.0))
                .andExpect(jsonPath("$.sector.description").value("irisSectorDescription"))
                .andExpect(jsonPath("$.sector.irisMapId").value(1L))
                .andExpect(jsonPath("$.artifact.id").value(1L))
                .andExpect(jsonPath("$.artifact.name").value("artifactName"))
                .andExpect(jsonPath("$.artifact.description").value("artifactDescription"))
                .andExpect(jsonPath("$.geometryJson").value("geometryJson"))
                .andExpect(jsonPath("$.confidenceScore").value(0.66))
                .andExpect(jsonPath("$.isGenerated").value(false))
                .andExpect(jsonPath("$.isValidated").value(false));
    }

    @Test
    void createShouldReturn400WhenNoBody() throws Exception {
        mockMvc.perform(post("/api/irisFinding/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createShouldReturn404WhenEntityNotFound() throws Exception {
        IrisFindingCreateDto createDto = new IrisFindingCreateDto(
                1L,
                1L,
                1L,
                0.66,
                "geometryJson"
        );
        when(irisFindingService.create(any(IrisFindingCreateDto.class)))
                .thenThrow(new EntityNotFoundException("Iris image not found"));

        mockMvc.perform(post("/api/irisFinding/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris image not found"));
    }

    @Test
    void getByIdShouldReturn200WhenFound() throws Exception {
        IrisFindingDetailDto detailDto = createSampleDetailDto(1L);
        when(irisFindingService.getById(1L)).thenReturn(detailDto);

        mockMvc.perform(get("/api/irisFinding/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.irisImageImageId").value("imageId"))
                .andExpect(jsonPath("$.geometryJson").value("geometryJson"));
    }

    @Test
    void getByIdShouldReturn404WhenNotFound() throws Exception {
        when(irisFindingService.getById(1L))
                .thenThrow(new EntityNotFoundException("Iris finding not found"));

        mockMvc.perform(get("/api/irisFinding/id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris finding not found"));
    }

    @Test
    void updateShouldReturn200WhenSuccessful() throws Exception {
        IrisFindingUpdateDto updateDto = new IrisFindingUpdateDto(1L, 2L, 3L, "updatedGeometry", true);
        IrisFindingDetailDto detailDto = createSampleDetailDto(1L);

        when(irisFindingService.update(updateDto)).thenReturn(detailDto);

        mockMvc.perform(patch("/api/irisFinding/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateShouldReturn400WhenNoBody() throws Exception {
        mockMvc.perform(patch("/api/irisFinding/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateShouldReturn404WhenNotFound() throws Exception {
        IrisFindingUpdateDto updateDto = new IrisFindingUpdateDto(1L, 2L, 3L, "updatedGeometry", true);
        when(irisFindingService.update(any(IrisFindingUpdateDto.class)))
                .thenThrow(new EntityNotFoundException("Iris finding not found"));

        mockMvc.perform(patch("/api/irisFinding/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris finding not found"));
    }

    @Test
    void getAllShouldReturn200() throws Exception {
        List<IrisFindingListDto> listDtos = List.of(
                new IrisFindingListDto(1L, "artifact1", "sector1", 0.8, true, true),
                new IrisFindingListDto(2L, "artifact2", "sector2", 0.9, false, false)
        );
        when(irisFindingService.getAll()).thenReturn(listDtos);

        mockMvc.perform(get("/api/irisFinding"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].artifactName").value("artifact1"))
                .andExpect(jsonPath("$.[0].sectorName").value("sector1"))
                .andExpect(jsonPath("$.[0].confidenceScore").value(0.8))
                .andExpect(jsonPath("$.[0].isGenerated").value(true))
                .andExpect(jsonPath("$.[0].isValidated").value(true))
                .andExpect(jsonPath("$.[1].id").value(2L));
    }

    @Test
    void getByImageIdShouldReturn200() throws Exception {
        List<IrisFindingListDto> listDtos = List.of(
                new IrisFindingListDto(1L, "artifact1", "sector1", 0.8, true, true)
        );
        when(irisFindingService.getByIrisImageId(1L)).thenReturn(listDtos);

        mockMvc.perform(get("/api/irisFinding/image/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].artifactName").value("artifact1"));
    }

    @Test
    void getByLabelCodeShouldReturn200() throws Exception {
        List<IrisFindingListDto> listDtos = List.of(
                new IrisFindingListDto(1L, "artifact1", "sector1", 0.8, true, true)
        );
        when(irisFindingService.getByArtifactLabelCode("LABEL_A")).thenReturn(listDtos);

        mockMvc.perform(get("/api/irisFinding/label/LABEL_A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].artifactName").value("artifact1"));
    }

    @Test
    void getByIsValidatedShouldReturn200() throws Exception {
        List<IrisFindingListDto> listDtos = List.of(
                new IrisFindingListDto(1L, "artifact1", "sector1", 0.8, true, true)
        );
        when(irisFindingService.getByIsValidated(true)).thenReturn(listDtos);

        mockMvc.perform(get("/api/irisFinding/validated/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].isValidated").value(true));
    }

    @Test
    void deleteShouldReturn204WhenSuccessful() throws Exception {
        doNothing().when(irisFindingService).delete(1L);

        mockMvc.perform(delete("/api/irisFinding/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404WhenNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Iris finding not found"))
                .when(irisFindingService).delete(1L);

        mockMvc.perform(delete("/api/irisFinding/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris finding not found"));
    }
}
