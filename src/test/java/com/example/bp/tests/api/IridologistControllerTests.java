package com.example.bp.tests.api;

import com.example.bp.api.controllers.IridologistController;
import com.example.bp.api.dto.IridologistCreateDto;
import com.example.bp.api.dto.IridologistDetailDto;
import com.example.bp.api.dto.IridologistListDto;
import com.example.bp.api.dto.IridologistUpdateDto;
import com.example.bp.exception.UsernameAlreadyExistsException;
import com.example.bp.service.IridologistService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IridologistController.class)
public class IridologistControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IridologistService iridologistService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerIridologistShouldReturn200WhenSuccessful() throws Exception {
        IridologistCreateDto createDto = new IridologistCreateDto("username", "firstName", "lastName", "pwd");
        IridologistDetailDto detailDto = new IridologistDetailDto(1L, createDto.firstName(), createDto.lastName(), createDto.username());

        when(iridologistService.register(any(IridologistCreateDto.class))).thenReturn(detailDto);

        mockMvc.perform(post("/api/iridologist/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(detailDto.id()))
                .andExpect(jsonPath("$.firstName").value(detailDto.firstName()))
                .andExpect(jsonPath("$.lastName").value(detailDto.lastName()))
                .andExpect(jsonPath("$.username").value(detailDto.username()));
    }

    @Test
    void registerIridologistShouldReturn400WhenNoBody() throws Exception {
        mockMvc.perform(post("/api/iridologist/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerIridologistShouldReturn409WhenUsernameAlreadyExists() throws Exception {
        IridologistCreateDto createDto = new IridologistCreateDto("existingUser", "firstName", "lastName", "pwd");
        when(iridologistService.register(any(IridologistCreateDto.class)))
                .thenThrow(new UsernameAlreadyExistsException("Username already exists"));

        mockMvc.perform(post("/api/iridologist/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Username already exists"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void getIridologistByIdShouldReturn200WhenSuccessful() throws Exception {
        IridologistDetailDto detailDto = new IridologistDetailDto(1L, "firstName", "lastName", "username");

        when(iridologistService.getById(1L)).thenReturn(detailDto);

        mockMvc.perform(get("/api/iridologist/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void getIridologistByIdShouldReturn404WhenNotFound() throws Exception {
        when(iridologistService.getById(1L))
                .thenThrow(new EntityNotFoundException("Iridologist not found."));

        mockMvc.perform(get("/api/iridologist/id/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iridologist not found."))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getAllIridologistsShouldReturn200() throws Exception {
        List<IridologistListDto> listDtos = List.of(
                new IridologistListDto("firstName1", "lastName1"),
                new IridologistListDto("firstName2", "lastName2")
        );
        when(iridologistService.getAll()).thenReturn(listDtos);

        mockMvc.perform(get("/api/iridologist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value("firstName1"))
                .andExpect(jsonPath("$.[0].lastName").value("lastName1"))
                .andExpect(jsonPath("$.[1].firstName").value("firstName2"))
                .andExpect(jsonPath("$.[1].lastName").value("lastName2"));
    }

    @Test
    void getIridologistByFirstNameShouldReturn200WhenExists() throws Exception {
        List<IridologistListDto> listDtos = List.of(
                new IridologistListDto("firstName", "lastName1"),
                new IridologistListDto("firstName", "lastName2")
        );

        when(iridologistService.getByFirstName("firstName")).thenReturn(listDtos);

        mockMvc.perform(get("/api/iridologist/firstName/firstName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value("firstName"))
                .andExpect(jsonPath("$.[0].lastName").value("lastName1"))
                .andExpect(jsonPath("$.[1].firstName").value("firstName"))
                .andExpect(jsonPath("$.[1].lastName").value("lastName2"));
    }

    @Test
    void getIridologistByFirstNameShouldReturn404WhenNotFound() throws Exception {
        when(iridologistService.getByFirstName("fakeFirstName"))
                .thenThrow(new EntityNotFoundException("fakeFirstName does not exist."));

        mockMvc.perform(get("/api/iridologist/firstName/fakeFirstName"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("fakeFirstName does not exist."))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getIridologistByLastNameShouldReturn200WhenExists() throws Exception {
        List<IridologistListDto> listDtos = List.of(
                new IridologistListDto("firstName1", "Smith"),
                new IridologistListDto("firstName2", "Smith")
        );

        when(iridologistService.getByLastName("Smith")).thenReturn(listDtos);

        mockMvc.perform(get("/api/iridologist/lastName/Smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].lastName").value("Smith"))
                .andExpect(jsonPath("$.[1].lastName").value("Smith"));
    }

    @Test
    void getIridologistByLastNameShouldReturn404WhenNotFound() throws Exception {
        when(iridologistService.getByLastName("fakeLastName"))
                .thenThrow(new EntityNotFoundException("fakeLastName does not exist."));

        mockMvc.perform(get("/api/iridologist/lastName/fakeLastName"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("fakeLastName does not exist."))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getIridologistByUsernameShouldReturn200WhenExists() throws Exception {
        IridologistDetailDto detailDto = new IridologistDetailDto(1L, "firstName", "lastName", "dr_smith");

        when(iridologistService.getByUsername("dr_smith")).thenReturn(detailDto);

        mockMvc.perform(get("/api/iridologist/username/dr_smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("dr_smith"));
    }

    @Test
    void getIridologistByUsernameShouldReturn404WhenNotFound() throws Exception {
        when(iridologistService.getByUsername("fakeUser"))
                .thenThrow(new EntityNotFoundException("Username not found."));

        mockMvc.perform(get("/api/iridologist/username/fakeUser"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Username not found."));
    }

    @Test
    void updateIridologistShouldReturn204WhenSuccessful() throws Exception {
        IridologistUpdateDto updateDto = new IridologistUpdateDto(Optional.of("newFirstName"), Optional.of("newLastName"), Optional.empty());

        mockMvc.perform(patch("/api/iridologist/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateIridologistShouldReturn404WhenNotFound() throws Exception {
        IridologistUpdateDto updateDto = new IridologistUpdateDto(Optional.of("newFirstName"), Optional.of("newLastName"), Optional.of("password"));

        doThrow(new EntityNotFoundException("Iridologist not found."))
                .when(iridologistService).update(eq(1L), any(IridologistUpdateDto.class));

        mockMvc.perform(patch("/api/iridologist/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iridologist not found."));
    }

    @Test
    void deleteIridologistShouldReturn204WhenSuccessful() throws Exception {
        doNothing().when(iridologistService).delete(1L);

        mockMvc.perform(delete("/api/iridologist/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteIridologistShouldReturn404WhenNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Iridologist not found."))
                .when(iridologistService).delete(1L);

        mockMvc.perform(delete("/api/iridologist/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Iridologist not found."));
    }
}
