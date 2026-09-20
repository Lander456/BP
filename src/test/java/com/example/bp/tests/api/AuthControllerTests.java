package com.example.bp.tests.api;

import com.example.bp.api.controllers.AuthController;
import com.example.bp.api.dto.IridologistDetailDto;
import com.example.bp.api.dto.LoginRequestDto;
import com.example.bp.security.JwtService;
import com.example.bp.service.IridologistService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private IridologistService iridologistService;

    @Test
    void loginShouldReturn200AndTokenWhenCredentialsAreValid() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("tadeas", "password123");
        UserDetails userDetails = new User("tadeas", "password123", Collections.emptyList());
        IridologistDetailDto detailDto = new IridologistDetailDto(1L, "Tadeas", "Novak", "tadeas");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        when(userDetailsService.loadUserByUsername("tadeas")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("mocked-jwt-token");
        when(iridologistService.getByUsername("tadeas")).thenReturn(detailDto);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.firstName").value("Tadeas"))
                .andExpect(jsonPath("$.user.lastName").value("Novak"))
                .andExpect(jsonPath("$.user.username").value("tadeas"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("tadeas");
        verify(jwtService).generateToken(userDetails);
        verify(iridologistService).getByUsername("tadeas");
    }

    @Test
    void loginShouldPassCorrectCredentialsToAuthenticationManager() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("john_doe", "mySecretPassword");
        UserDetails userDetails = new User("john_doe", "mySecretPassword", Collections.emptyList());
        IridologistDetailDto detailDto = new IridologistDetailDto(2L, "John", "Doe", "john_doe");

        when(userDetailsService.loadUserByUsername("john_doe")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwt-token-xyz");
        when(iridologistService.getByUsername("john_doe")).thenReturn(detailDto);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk());

        verify(authenticationManager).authenticate(
                refEq(new UsernamePasswordAuthenticationToken("john_doe", "mySecretPassword"))
        );
    }

    @Test
    void loginShouldReturn401WhenBadCredentials() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("tadeas", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Bad credentials"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(iridologistService);
    }

    @Test
    void loginShouldReturn404WhenUserNotFoundInUserDetailsService() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("unknown_user", "password");

        when(userDetailsService.loadUserByUsername("unknown_user"))
                .thenThrow(new UsernameNotFoundException("User not found: unknown_user"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found: unknown_user"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("unknown_user");
        verifyNoInteractions(jwtService);
        verifyNoInteractions(iridologistService);
    }

    @Test
    void loginShouldReturn404WhenIridologistNotFoundInService() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("tadeas", "password123");
        UserDetails userDetails = new User("tadeas", "password123", Collections.emptyList());

        when(userDetailsService.loadUserByUsername("tadeas")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("mocked-jwt-token");
        when(iridologistService.getByUsername("tadeas"))
                .thenThrow(new EntityNotFoundException("Iridologist not found with username: tadeas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Iridologist not found with username: tadeas"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("tadeas");
        verify(jwtService).generateToken(userDetails);
        verify(iridologistService).getByUsername("tadeas");
    }

    @Test
    void loginShouldReturn400WhenNoRequestBody() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(iridologistService);
    }
}
