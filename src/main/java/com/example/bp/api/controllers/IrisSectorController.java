package com.example.bp.api.controllers;

import com.example.bp.api.dto.IrisSectorCreateDto;
import com.example.bp.api.dto.IrisSectorDetailDto;
import com.example.bp.api.dto.IrisSectorListDto;
import com.example.bp.api.dto.IrisSectorUpdateDto;
import com.example.bp.service.IrisSectorService;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sector")
public class IrisSectorController {

    private final IrisSectorService irisSectorService;

    public IrisSectorController(IrisSectorService irisSectorService) {
        this.irisSectorService = irisSectorService;
    }

    @PostMapping(
            path="/create"
    )
    public ResponseEntity<IrisSectorDetailDto> create(@NonNull IrisSectorCreateDto createDto) {
        IrisSectorDetailDto detailDto = irisSectorService.create(createDto);
        return ResponseEntity.ok(detailDto);
    }

    @PatchMapping(
            path="/update"
    )
    public ResponseEntity<IrisSectorDetailDto> update(@NonNull IrisSectorUpdateDto updateDto) {
        IrisSectorDetailDto detailDto = irisSectorService.update(updateDto);
        return ResponseEntity.ok(detailDto);
    }

    @GetMapping(
            path="/map/{mapId}"
    )
    public ResponseEntity<List<IrisSectorListDto>> getByMapId(@PathVariable Long mapId) {
        List<IrisSectorListDto> listDtos = irisSectorService.getByMapId(mapId);
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping(
            path="/map/{mapId}/{irisSectorName}"
    )
    public ResponseEntity<IrisSectorDetailDto> getByMapIdAndName(@PathVariable Long mapId, @PathVariable String irisSectorName) {
        IrisSectorDetailDto detailDto = irisSectorService.getByMapIdAndName(mapId, irisSectorName);
        return ResponseEntity.ok(detailDto);
    }

    @DeleteMapping(
            path="/irisSector/delete/{id}"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        irisSectorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
