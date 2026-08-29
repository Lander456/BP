package com.example.bp.api.controllers;

import com.example.bp.api.dto.PatientCreateDto;
import com.example.bp.api.dto.PatientDetailDto;
import com.example.bp.api.dto.PatientListDto;
import com.example.bp.api.dto.PatientUpdateDto;
import com.example.bp.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(
            path="/create"
    )
    public ResponseEntity<PatientDetailDto> create(PatientCreateDto createDto) {
        PatientDetailDto detailDto = patientService.create(createDto);
        return ResponseEntity.ok(detailDto);
    }

    @GetMapping(
            path="/{id}"
    )
    public ResponseEntity<PatientDetailDto> getById(@PathVariable Long id) {
        PatientDetailDto detailDto = patientService.getById(id);
        return ResponseEntity.ok(detailDto);
    }

    @GetMapping(
            path="/all"
    )
    public ResponseEntity<List<PatientListDto>> getAll() {
        List<PatientListDto> listDtos = patientService.getAll();
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping(
            path="/filterFirstName/{firstName}"
    )
    public ResponseEntity<List<PatientListDto>> getByFirstName(@PathVariable String firstName) {
        List<PatientListDto> listDtos = patientService.getByFirstName(firstName);
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping(
            path="/filterLastName/{lastName}"
    )
    public ResponseEntity<List<PatientListDto>> getByLastName(@PathVariable String lastName) {
        List<PatientListDto> listDtos = patientService.getByLastName(lastName);
        return ResponseEntity.ok(listDtos);
    }

    @PatchMapping(
            path="/update/{id}"
    )
    public ResponseEntity<Void> update(@PathVariable Long id, PatientUpdateDto updateDto) {
        patientService.update(id, updateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(
            path="/delete/{id}"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
