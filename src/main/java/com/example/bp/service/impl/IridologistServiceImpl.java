package com.example.bp.service.impl;

import com.example.bp.api.dto.IridologistCreateDto;
import com.example.bp.api.dto.IridologistDetailDto;
import com.example.bp.api.dto.IridologistListDto;
import com.example.bp.api.dto.IridologistUpdateDto;
import com.example.bp.api.mapper.IridologistMapper;
import com.example.bp.dal.entity.Iridologist;
import com.example.bp.dal.repository.IridologistRepository;
import com.example.bp.exception.UsernameAlreadyExistsException;
import com.example.bp.service.IridologistService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IridologistServiceImpl implements IridologistService {

    private final IridologistRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final IridologistMapper mapper;

    @Override
    public IridologistDetailDto register(@NonNull IridologistCreateDto dto) {
        if (repository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException("The username '" + dto.username() + "' is already in use.");
        }

        Iridologist iridologist = mapper.toEntity(dto);

        String encodedPassword = passwordEncoder.encode(dto.password());
        iridologist.setPassword(encodedPassword);

        Iridologist saved = repository.save(iridologist);
        return mapper.toDetailDto(saved);
    }

    @Override
    public IridologistDetailDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDetailDto)
                .orElseThrow(() -> new EntityNotFoundException("Iridologist not found with ID: " + id));
    }

    @Override
    public List<IridologistListDto> getAll() {
        List<Iridologist> entities = repository.findAll();
        return mapper.toListDtoList(entities);
    }

    @Override
    public void update(Long id, IridologistUpdateDto dto) {
        Iridologist entity = mapper.toEntity(dto);
        entity.setId(id);
        repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
