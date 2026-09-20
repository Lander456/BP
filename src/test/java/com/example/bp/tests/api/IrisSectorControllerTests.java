package com.example.bp.tests.api;

import com.example.bp.api.controllers.IrisSectorController;
import com.example.bp.api.dto.IrisSectorCreateDto;
import com.example.bp.api.dto.IrisSectorDetailDto;
import com.example.bp.api.dto.IrisSectorListDto;
import com.example.bp.api.dto.IrisSectorUpdateDto;
import com.example.bp.service.IrisSectorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IrisSectorController.class)
public class IrisSectorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IrisSectorService irisSectorService;

    @Test
    void createShouldReturn200WhenSuccessful() throws Exception {
        IrisSectorDetailDto detailDto = new IrisSectorDetailDto(
                1L,
                "SectorA",
                0.0,
                90.0,
                10.0,
                20.0,
                "Sector description",
                1L
        );

        when(irisSectorService.create(any(IrisSectorCreateDto.class))).thenReturn(detailDto);

        mockMvc.perform(post("/sector/create")
                        .param("irisMapId", "1")
                        .param("name", "SectorA")
                        .param("startAngle", "0.0")
                        .param("endAngle", "90.0")
                        .param("innerRadius", "10.0")
                        .param("outerRadius", "20.0")
                        .param("description", "Sector description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("SectorA"))
                .andExpect(jsonPath("$.startAngle").value(0.0))
                .andExpect(jsonPath("$.endAngle").value(90.0))
                .andExpect(jsonPath("$.innerRadius").value(10.0))
                .andExpect(jsonPath("$.outerRadius").value(20.0))
                .andExpect(jsonPath("$.description").value("Sector description"))
                .andExpect(jsonPath("$.irisMapId").value(1L));
    }

    @Test
    void createShouldReturn404WhenIrisMapNotFound() throws Exception {
        when(irisSectorService.create(any(IrisSectorCreateDto.class)))
                .thenThrow(new EntityNotFoundException("Iris map not found"));

        mockMvc.perform(post("/sector/create")
                        .param("irisMapId", "99")
                        .param("name", "SectorA")
                        .param("startAngle", "0.0")
                        .param("endAngle", "90.0"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris map not found"));
    }

    @Test
    void updateShouldReturn200WhenSuccessful() throws Exception {
        IrisSectorDetailDto detailDto = new IrisSectorDetailDto(
                1L,
                "UpdatedSector",
                0.0,
                90.0,
                10.0,
                20.0,
                "Updated description",
                1L
        );

        when(irisSectorService.update(any(IrisSectorUpdateDto.class))).thenReturn(detailDto);

        mockMvc.perform(patch("/sector/update")
                        .param("id", "1")
                        .param("name", "UpdatedSector")
                        .param("startAngle", "0.0")
                        .param("endAngle", "90.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("UpdatedSector"));
    }

    @Test
    void updateShouldReturn404WhenNotFound() throws Exception {
        when(irisSectorService.update(any(IrisSectorUpdateDto.class)))
                .thenThrow(new EntityNotFoundException("Iris sector not found"));

        mockMvc.perform(patch("/sector/update")
                        .param("id", "99")
                        .param("name", "UpdatedSector")
                        .param("startAngle", "0.0")
                        .param("endAngle", "90.0"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris sector not found"));
    }

    @Test
    void getByMapIdShouldReturn200() throws Exception {
        List<IrisSectorListDto> listDtos = List.of(
                new IrisSectorListDto(1L, "Sector1", 0.0, 90.0),
                new IrisSectorListDto(2L, "Sector2", 90.0, 180.0)
        );

        when(irisSectorService.getByMapId(1L)).thenReturn(listDtos);

        mockMvc.perform(get("/sector/map/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("Sector1"))
                .andExpect(jsonPath("$.[0].startAngle").value(0.0))
                .andExpect(jsonPath("$.[0].endAngle").value(90.0))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].name").value("Sector2"));
    }

    @Test
    void getByMapIdShouldReturn404WhenMapNotFound() throws Exception {
        when(irisSectorService.getByMapId(99L))
                .thenThrow(new EntityNotFoundException("Iris map not found"));

        mockMvc.perform(get("/sector/map/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris map not found"));
    }

    @Test
    void getByMapIdAndNameShouldReturn200WhenFound() throws Exception {
        IrisSectorDetailDto detailDto = new IrisSectorDetailDto(
                1L,
                "Sector1",
                0.0,
                90.0,
                10.0,
                20.0,
                "Desc",
                1L
        );

        when(irisSectorService.getByMapIdAndName(1L, "Sector1")).thenReturn(detailDto);

        mockMvc.perform(get("/sector/map/1/Sector1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Sector1"))
                .andExpect(jsonPath("$.irisMapId").value(1L));
    }

    @Test
    void getByMapIdAndNameShouldReturn404WhenNotFound() throws Exception {
        when(irisSectorService.getByMapIdAndName(1L, "NonExistent"))
                .thenThrow(new EntityNotFoundException("Iris sector not found"));

        mockMvc.perform(get("/sector/map/1/NonExistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris sector not found"));
    }

    @Test
    void deleteShouldReturn204WhenSuccessful() throws Exception {
        doNothing().when(irisSectorService).delete(1L);

        mockMvc.perform(delete("/sector/irisSector/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404WhenNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Iris sector not found"))
                .when(irisSectorService).delete(1L);

        mockMvc.perform(delete("/sector/irisSector/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iris sector not found"));
    }
}
