package com.example.bp.api.controllers;

import com.example.bp.api.dto.IrisImageCreateDto;
import com.example.bp.api.dto.IrisImageDetailDto;
import com.example.bp.api.dto.IrisImageListDto;
import com.example.bp.service.IrisImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/image")
public class IrisImageController {

    private final IrisImageService irisImageService;

    public IrisImageController(IrisImageService irisImageService) {
        this.irisImageService = irisImageService;
    }

    @PostMapping(
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<IrisImageDetailDto> uploadNewImage(@RequestPart("imageData")IrisImageCreateDto createDto, @RequestPart("file") MultipartFile file) {
        IrisImageDetailDto detailDto = irisImageService.upload(file, createDto);
        return ResponseEntity.ok(detailDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IrisImageDetailDto> getById(@PathVariable Long id) {
        IrisImageDetailDto detailDto = irisImageService.getById(id);
        return ResponseEntity.ok(detailDto);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<IrisImageListDto>> getByPatientId(@PathVariable Long patientId) {
        List<IrisImageListDto> listDtos = irisImageService.getByPatientId(patientId);
        return ResponseEntity.ok(listDtos);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        irisImageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
