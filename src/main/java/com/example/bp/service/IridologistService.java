package com.example.bp.service;

import com.example.bp.api.dto.IridologistCreateDto;
import com.example.bp.api.dto.IridologistDetailDto;
import com.example.bp.api.dto.IridologistListDto;
import com.example.bp.api.dto.IridologistUpdateDto;

import java.util.List;

public interface IridologistService {
    IridologistDetailDto register(IridologistCreateDto dto);
    IridologistDetailDto getById(Long id);
    List<IridologistListDto> getByFirstName(String firstName);
    List<IridologistListDto> getByLastName(String lastName);
    List<IridologistListDto> getByUsername(String username);
    List<IridologistListDto> getAll();
    void update(Long id, IridologistUpdateDto dto);
    void delete(Long id);
}
