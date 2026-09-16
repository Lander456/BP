package com.example.bp.tests.api;

import com.example.bp.api.controllers.ArtifactController;
import com.example.bp.api.dto.ArtifactCreateDto;
import com.example.bp.api.dto.ArtifactDetailDto;
import com.example.bp.api.dto.ArtifactListDto;
import com.example.bp.api.dto.ArtifactUpdateDto;
import com.example.bp.service.ArtifactService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtifactController.class)
public class ArtifactControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtifactService artifactService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getArtifactShouldReturn200WhenFound() throws Exception {
        ArtifactDetailDto detailDto = new ArtifactDetailDto(1L, "mockArtifact", "mockArtifactDescription");
        when(artifactService.getById(1L)).thenReturn(detailDto);

        mockMvc.perform(get("/api/artifact/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("mockArtifact"))
                .andExpect(jsonPath("$.description").value("mockArtifactDescription"));
    }

    @Test
    void getArtifactShouldReturn404WhenNotFound() throws Exception {
        when(artifactService.getById(1L))
                .thenThrow(new EntityNotFoundException("Artifact not found"));

        mockMvc.perform(get("/api/artifact/id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Artifact not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getAllArtifactsShouldReturn200WithAllArtifactsAlways() throws Exception {
        List<ArtifactListDto> artifactListDtos = List.of(
            new ArtifactListDto(1L, "artifactName1"),
            new ArtifactListDto(2L, "artifactName2")
        );
        when(artifactService.getAll()).thenReturn(artifactListDtos);

        mockMvc.perform(get("/api/artifact/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("artifactName1"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("artifactName2"));
    }

    @Test
    void getArtifactByLabelCodeShouldReturn200WhenExists() throws Exception {
        ArtifactDetailDto detailDto = new ArtifactDetailDto(1L, "artifactName", "artifactDescription");
        when(artifactService.getByLabelCode("artifactLabelCode")).thenReturn(detailDto);

        mockMvc.perform(get("/api/artifact/label/artifactLabelCode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("artifactName"))
                .andExpect(jsonPath("$.description").value("artifactDescription"));
    }

    @Test
    void getArtifactByLabelCodeShouldReturn404WhenNotFound() throws Exception {
        when(artifactService.getByLabelCode("non-existing labelCode"))
                .thenThrow(new EntityNotFoundException("Artifact not found"));

        mockMvc.perform(get("/api/artifact/label/non-existing labelCode"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Artifact not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createArtifactShouldReturn200WhenSuccessful() throws Exception {
        ArtifactCreateDto createDto = new ArtifactCreateDto("newArtifactName", "newArtifactDescription", "newLabelCode");
        ArtifactDetailDto detailDto = new ArtifactDetailDto(1L, createDto.name(), createDto.description());

        when(artifactService.create(createDto)).thenReturn(detailDto);

        mockMvc.perform(post("/api/artifact/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("newArtifactName"))
                .andExpect(jsonPath("$.description").value("newArtifactDescription"));
    }

    @Test
    void createArtifactShouldReturn400WhenNoBody() throws Exception {
        mockMvc.perform(post("/api/artifact/create")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateArtifactShouldReturn200WhenSuccessful() throws Exception {
        ArtifactUpdateDto updateDto = new ArtifactUpdateDto(1L, "artifactName", "artifactDescription");
        ArtifactDetailDto detailDto = new ArtifactDetailDto(updateDto.id(), updateDto.name(), updateDto.description());

        when(artifactService.update(updateDto)).thenReturn(detailDto);

        mockMvc.perform(patch("/api/artifact/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(detailDto.id()))
                .andExpect(jsonPath("$.name").value(detailDto.name()))
                .andExpect(jsonPath("$.description").value(detailDto.description()));
    }

    @Test
    void updateArtifactShouldReturn400WhenNoBody() throws Exception {
        mockMvc.perform(patch("/api/artifact/update")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteArtifactShouldReturn200WhenSuccessful() throws Exception {
        mockMvc.perform(delete("/api/artifact/delete/1"))
                .andExpect(status().is2xxSuccessful());
    }
}
