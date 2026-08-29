package com.example.bp.api.controllers;

import com.example.bp.api.dto.IrisFindingCreateDto;
import com.example.bp.api.dto.IrisFindingDetailDto;
import com.example.bp.api.dto.IrisFindingListDto;
import com.example.bp.api.dto.IrisFindingUpdateDto;
import com.example.bp.service.IrisFindingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/irisFinding")
public class IrisFindingController {

    private final IrisFindingService irisFindingService;

    public IrisFindingController(IrisFindingService irisFindingService) {
        this.irisFindingService = irisFindingService;
    }

    @PostMapping("/create")
    public ResponseEntity<IrisFindingDetailDto> createNewIrisFinding(@RequestBody IrisFindingCreateDto createDto) {
        IrisFindingDetailDto detailDto = irisFindingService.create(createDto);
        return ResponseEntity.ok(detailDto);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<IrisFindingDetailDto> getIrisFindingById(@PathVariable Long id) {
        IrisFindingDetailDto detailDto = irisFindingService.getById(id);
        return ResponseEntity.ok(detailDto);
    }

    @PatchMapping("/update")
    public ResponseEntity<IrisFindingDetailDto> updateIrisFinding(@RequestBody IrisFindingUpdateDto updateDto) {
        IrisFindingDetailDto detailDto = irisFindingService.update(updateDto);
        return ResponseEntity.ok(detailDto);
    }

    @GetMapping
    public ResponseEntity<List<IrisFindingListDto>> getAllIrisFindings() {
        List<IrisFindingListDto> listDtos = irisFindingService.getAll();
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<List<IrisFindingListDto>> getIrisFindingsByImageId(@PathVariable Long imageId) {
        List<IrisFindingListDto> listDtos = irisFindingService.getByIrisImageId(imageId);
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping("/label/{labelCode}")
    public ResponseEntity<List<IrisFindingListDto>> getIrisFindingsByLabelCode(@PathVariable String labelCode) {
        List<IrisFindingListDto> listDtos = irisFindingService.getByArtifactLabelCode(labelCode);
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping("/validated/{isValidated}")
    public ResponseEntity<List<IrisFindingListDto>> getByIsValidated(@PathVariable Boolean isValidated) {
        List<IrisFindingListDto> listDtos = irisFindingService.getByIsValidated(isValidated);
        return ResponseEntity.ok(listDtos);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIrisFinding(@PathVariable Long id) {
        irisFindingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
