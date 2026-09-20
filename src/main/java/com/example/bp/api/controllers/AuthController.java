package com.example.bp.api.controllers;

import com.example.bp.api.dto.AuthResponseDto;
import com.example.bp.api.dto.IridologistDetailDto;
import com.example.bp.api.dto.LoginRequestDto;
import com.example.bp.security.JwtService;
import com.example.bp.service.IridologistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final IridologistService iridologistService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtService jwtService,
                          IridologistService iridologistService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.iridologistService = iridologistService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.username());
        String token = jwtService.generateToken(userDetails);
        IridologistDetailDto detailDto = iridologistService.getByUsername(loginRequestDto.username());

        return ResponseEntity.ok(new AuthResponseDto(token, detailDto));
    }
}
