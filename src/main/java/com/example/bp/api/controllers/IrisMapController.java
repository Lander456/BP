package com.example.bp.api.controllers;

import com.example.bp.api.dto.*;
import com.example.bp.service.IrisMapService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/map")
public class IrisMapController {

    private final IrisMapService irisMapService;

    public IrisMapController(IrisMapService irisMapService) {
        this.irisMapService = irisMapService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IrisMapDetailDto> getById(@PathVariable Long id) {
        IrisMapDetailDto detailDto = irisMapService.getById(id);
        return ResponseEntity.ok(detailDto);
    }

    @PostMapping(
            path="/upload",
            consumes= MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<IrisMapDetailDto> upload(@RequestPart("file") MultipartFile file, @RequestPart("imageData")IrisMapCreateDto createDto) {
        IrisMapDetailDto detailDto = irisMapService.create(createDto, file);
        return ResponseEntity.ok(detailDto);
    }

    @PatchMapping("/update")
    public ResponseEntity<IrisMapDetailDto> update(@RequestBody IrisMapUpdateDto updateDto) {
        IrisMapDetailDto detailDto = irisMapService.update(updateDto);
        return ResponseEntity.ok(detailDto);
    }

    @GetMapping("/iridologist/{iridologistId}")
    public ResponseEntity<List<IrisMapListDto>> getByIridologistId(@PathVariable Long iridologistId) {
        List<IrisMapListDto> listDtos = irisMapService.getByIridologistId(iridologistId);
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping
    public ResponseEntity<List<IrisMapListDto>> getAll() {
        List<IrisMapListDto> listDtos = irisMapService.getAll();
        return ResponseEntity.ok(listDtos);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        irisMapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
