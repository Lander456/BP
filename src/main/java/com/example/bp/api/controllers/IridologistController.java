package com.example.bp.api.controllers;

import com.example.bp.api.dto.IridologistCreateDto;
import com.example.bp.api.dto.IridologistDetailDto;
import com.example.bp.api.dto.IridologistListDto;
import com.example.bp.api.dto.IridologistUpdateDto;
import com.example.bp.service.IridologistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iridologist")
public class IridologistController {

    private final IridologistService iridologistService;

    public IridologistController(IridologistService iridologistService) {
        this.iridologistService = iridologistService;
    }

    @PostMapping("/register")
    public ResponseEntity<IridologistDetailDto> registerIridologist(@RequestBody IridologistCreateDto createDto) {
        IridologistDetailDto iridologistDetailDto = iridologistService.register(createDto);
        return ResponseEntity.ok(iridologistDetailDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IridologistDetailDto> getIridologist(@PathVariable Long id) {
        IridologistDetailDto detailDto = iridologistService.getById(id);
        return ResponseEntity.ok(detailDto);
    }

    @GetMapping
    public ResponseEntity<List<IridologistListDto>> getAllIridologists() {
        List<IridologistListDto> listDtos = iridologistService.getAll();
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping("/{firstName}")
    public ResponseEntity<List<IridologistListDto>> getIridologistsByFirstName(@PathVariable String firstName) {
        List<IridologistListDto> listDtos = iridologistService.getByFirstName(firstName);
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping("/{lastName}")
    public ResponseEntity<List<IridologistListDto>> getIridologistsByLastName(@PathVariable String lastName) {
        List<IridologistListDto> listDtos = iridologistService.getByLastName(lastName);
        return ResponseEntity.ok(listDtos);
    }

    @GetMapping("/{username}")
    public ResponseEntity<IridologistDetailDto> getIridologistByUsername(@PathVariable String username) {
        IridologistDetailDto detailDto = iridologistService.getByUsername(username);
        return ResponseEntity.ok(detailDto);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updateIridologist(@PathVariable Long id, @RequestBody IridologistUpdateDto updateDto) {
        iridologistService.update(id, updateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIridologist(@PathVariable Long id) {
        iridologistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
