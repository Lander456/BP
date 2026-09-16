package com.example.bp.api.controllers;

import com.example.bp.api.dto.ArtifactCreateDto;
import com.example.bp.api.dto.ArtifactDetailDto;
import com.example.bp.api.dto.ArtifactListDto;
import com.example.bp.api.dto.ArtifactUpdateDto;
import com.example.bp.service.ArtifactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artifact")
public class ArtifactController {

    private final ArtifactService artifactService;

    public ArtifactController(ArtifactService artifactService) {
        this.artifactService = artifactService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArtifactListDto>> getAllArtifacts() {
        List<ArtifactListDto> artifactListDto = artifactService.getAll();
        return ResponseEntity.ok(artifactListDto);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ArtifactDetailDto> getArtifactById(@PathVariable Long id) {
        ArtifactDetailDto artifactDetailDto = artifactService.getById(id);
        return ResponseEntity.ok(artifactDetailDto);
    }

    @GetMapping("/label/{labelCode}")
    public ResponseEntity<ArtifactDetailDto> getByLabelCode(@PathVariable String labelCode) {
        ArtifactDetailDto artifactDetailDto = artifactService.getByLabelCode(labelCode);
        return ResponseEntity.ok(artifactDetailDto);
    }

    @PostMapping("/create")
    public ResponseEntity<ArtifactDetailDto> createNew(@RequestBody ArtifactCreateDto artifactCreateDto) {
        ArtifactDetailDto savedArtifact = artifactService.create(artifactCreateDto);
        return ResponseEntity.ok(savedArtifact);
    }

    @PatchMapping("/update")
    public ResponseEntity<ArtifactDetailDto> update(@RequestBody ArtifactUpdateDto artifactUpdateDto) {
        ArtifactDetailDto updatedArtifact = artifactService.update(artifactUpdateDto);
        return ResponseEntity.ok(updatedArtifact);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        artifactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
