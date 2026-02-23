package com.example.bp.service.impl;

import com.example.bp.api.dto.PatientCreateDto;
import com.example.bp.api.dto.PatientDetailDto;
import com.example.bp.api.dto.PatientListDto;
import com.example.bp.api.dto.PatientUpdateDto;
import com.example.bp.api.mapper.PatientMapper;
import com.example.bp.dal.entity.Patient;
import com.example.bp.dal.repository.PatientRepository;
import com.example.bp.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;

    @Override
    public PatientDetailDto create(PatientCreateDto dto) {
        Patient patient = mapper.toEntity(dto);

        Patient saved = repository.save(patient);
        return mapper.toDetailDto(saved);
    }

    @Override
    public PatientDetailDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDetailDto)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + id));
    }

    @Override
    public List<PatientListDto> getAll() {
        List<Patient> entities = repository.findAll();
        return mapper.toListDtoList(entities);
    }

    @Override
    public List<PatientListDto> getByFirstName(String firstName) {
        List<Patient> entities = repository.findByFirstName(firstName);
        return mapper.toListDtoList(entities);
    }

    @Override
    public List<PatientListDto> getByLastName(String lastName) {
        List<Patient> entities = repository.findByLastName(lastName);
        return mapper.toListDtoList(entities);
    }

    @Override
    public void update(Long id, PatientUpdateDto dto) {
        Patient entity = mapper.toEntity(dto);
        entity.setId(id);
        repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
