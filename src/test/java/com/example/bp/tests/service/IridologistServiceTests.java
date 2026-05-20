package com.example.bp.tests.service;

import com.example.bp.api.dto.IridologistCreateDto;
import com.example.bp.api.dto.IridologistDetailDto;
import com.example.bp.api.dto.IridologistListDto;
import com.example.bp.api.dto.IridologistUpdateDto;
import com.example.bp.api.mapper.IridologistMapperImpl;
import com.example.bp.dal.entity.Iridologist;
import com.example.bp.dal.repository.IridologistRepository;
import com.example.bp.service.impl.IridologistServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IridologistServiceTests {

    @InjectMocks
    private IridologistServiceImpl iridologistService;

    @Mock
    private IridologistMapperImpl iridologistMapper;

    @Mock
    private IridologistRepository iridologistRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void registeringNewIridologistRegisters() {
        IridologistCreateDto createDto = new IridologistCreateDto("username", "firstName", "lastName", "password");
        IridologistDetailDto detailDto = new IridologistDetailDto(1L, createDto.firstName(), createDto.lastName(), createDto.username());
        Iridologist newIridologist = new Iridologist(createDto.firstName(), createDto.lastName(), createDto.username(), createDto.password());
        String encodedPass = "encodedPass";

        when(iridologistRepository.existsByUsername(createDto.username())).thenReturn(false);
        when(iridologistMapper.toEntity(createDto)).thenReturn(newIridologist);
        when(passwordEncoder.encode(createDto.password())).thenReturn(encodedPass);
        when(iridologistRepository.save(newIridologist)).thenReturn(newIridologist);
        when(iridologistMapper.toDetailDto(newIridologist)).thenReturn(detailDto);

        iridologistService.register(createDto);

        verify(iridologistRepository).existsByUsername(any(String.class));
        verify(iridologistMapper).toEntity(any(IridologistCreateDto.class));
        verify(passwordEncoder).encode(any(String.class));
        verify(iridologistRepository).save(any(Iridologist.class));
        verify(iridologistMapper).toDetailDto(any(Iridologist.class));

        verifyNoMoreInteractions(iridologistRepository);
        verifyNoMoreInteractions(iridologistMapper);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    void registeringExistingUsernameThrows() {
        IridologistCreateDto dto = new IridologistCreateDto("username", "firstName", "lastName", "password");

        when(iridologistRepository.existsByUsername(dto.username())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> iridologistService.register(dto));
        assertTrue(exception.getMessage().contains("The username 'username' is already in use."));
    }

    @Test
    void gettingExistingIridologistByIdReturns() {
        Long id = 1L;
        Iridologist iridologist = new Iridologist("firstName", "lastName", "username", "password");
        IridologistDetailDto dto = new IridologistDetailDto(id, iridologist.getFirstName(), iridologist.getLastName(), iridologist.getUsername());

        when(iridologistRepository.findById(id)).thenReturn(Optional.of(iridologist));
        when(iridologistMapper.toDetailDto(iridologist)).thenReturn(dto);

        iridologistService.getById(id);

        verify(iridologistRepository).findById(1L);
        verify(iridologistMapper).toDetailDto(iridologist);

        verifyNoMoreInteractions(iridologistRepository);
        verifyNoMoreInteractions(iridologistMapper);
    }

    @Test
    void gettingNonExistingIridologistByIdThrows() {
        Long id = 1L;

        when(iridologistRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> iridologistService.getById(id));

        assertTrue(exception.getMessage().contains("Iridologist not found with ID: " + id));

        verifyNoMoreInteractions(iridologistRepository);
        verifyNoInteractions(iridologistMapper);
    }

    @Test
    void gettingAllIridologistsExecutes() {
        Iridologist iridologist = new Iridologist("firstName", "lastName", "username", "password");
        IridologistListDto dto = new IridologistListDto(iridologist.getFirstName(), iridologist.getLastName());
        List<Iridologist> allIridologists = List.of(iridologist);
        List<IridologistListDto> listDtoLst = List.of(dto);

        when(iridologistRepository.findAll()).thenReturn(allIridologists);
        when(iridologistMapper.toListDtoList(allIridologists)).thenReturn(listDtoLst);

        iridologistService.getAll();

        verify(iridologistRepository).findAll();
        verify(iridologistMapper).toListDtoList(allIridologists);

        verifyNoMoreInteractions(iridologistRepository);
        verifyNoMoreInteractions(iridologistMapper);
    }

    @Test
    void gettingIridologistsByFirstNameExecutes() {
        Iridologist iridologist = new Iridologist("firstName", "lastName", "username", "password");
        IridologistListDto dto = new IridologistListDto(iridologist.getFirstName(), iridologist.getLastName());
        List<Iridologist> foundIridologists = List.of(iridologist);
        List<IridologistListDto> listDtoList = List.of(dto);

        when(iridologistRepository.findByFirstName(iridologist.getFirstName())).thenReturn(foundIridologists);
        when(iridologistMapper.toListDtoList(foundIridologists)).thenReturn(listDtoList);

        iridologistService.getByFirstName(iridologist.getFirstName());

        verify(iridologistRepository).findByFirstName(iridologist.getFirstName());
        verify(iridologistMapper).toListDtoList(foundIridologists);

        verifyNoMoreInteractions(iridologistRepository);
        verifyNoMoreInteractions(iridologistMapper);
    }

    @Test
    void gettingIridologistsByLastNameExecutes() {
        Iridologist iridologist = new Iridologist("firstName", "lastName", "username", "password");
        IridologistListDto dto = new IridologistListDto(iridologist.getFirstName(), iridologist.getLastName());
        List<Iridologist> foundIridologists = List.of(iridologist);
        List<IridologistListDto> listDtoList = List.of(dto);

        when(iridologistRepository.findByLastName(iridologist.getLastName())).thenReturn(foundIridologists);
        when(iridologistMapper.toListDtoList(foundIridologists)).thenReturn(listDtoList);

        iridologistService.getByLastName(iridologist.getLastName());

        verify(iridologistRepository).findByLastName(iridologist.getLastName());
        verify(iridologistMapper).toListDtoList(foundIridologists);

        verifyNoMoreInteractions(iridologistRepository);
        verifyNoMoreInteractions(iridologistMapper);
    }

    @Test
    void gettingIridologistByExistingUsernameExecutes() {
        Iridologist iridologist = new Iridologist("firstName", "lastName", "username", "password");
        IridologistDetailDto dto = new IridologistDetailDto(1L, iridologist.getFirstName(), iridologist.getLastName(), iridologist.getUsername());

        when(iridologistRepository.findByUsername(iridologist.getUsername())).thenReturn(Optional.of(iridologist));
        when(iridologistMapper.toDetailDto(iridologist)).thenReturn(dto);

        iridologistService.getByUsername(iridologist.getUsername());

        verify(iridologistRepository).findByUsername(iridologist.getUsername());
        verify(iridologistMapper).toDetailDto(iridologist);

        verifyNoMoreInteractions(iridologistRepository);
        verifyNoMoreInteractions(iridologistMapper);
    }

    @Test
    void gettingIridologistByNonExistingUsernameThrows() {
        String nonExistingUsername = "nonExistingUsername";

        when(iridologistRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> iridologistService.getByUsername(nonExistingUsername));

        assertTrue(exception.getMessage().contains("Iridologist not found with username: " + nonExistingUsername));

        verify(iridologistRepository).findByUsername(nonExistingUsername);

        verifyNoMoreInteractions(iridologistRepository);
        verifyNoInteractions(iridologistMapper);
    }

    @Test
    void updatingIridologistExecutes() {
        Long id = 1L;
        Iridologist updatedIridologist = new Iridologist("firstName", "lastName", "username", "password");
        IridologistUpdateDto dto = new IridologistUpdateDto(Optional.of("newFirstName"), Optional.empty(), Optional.empty());

        when(iridologistMapper.toEntity(dto)).thenReturn(updatedIridologist);

        iridologistService.update(id, dto);

        ArgumentCaptor<Iridologist> captor = ArgumentCaptor.forClass(Iridologist.class);

        verify(iridologistRepository).save(captor.capture());

        verifyNoMoreInteractions(iridologistRepository);
        verifyNoMoreInteractions(iridologistMapper);

        Iridologist savedEntity = captor.getValue();
        assertEquals(id,  savedEntity.getId());
    }

    @Test
    void deleteExecutes() {
        Long id = 1L;

        iridologistService.delete(id);

        verify(iridologistRepository).deleteById(id);

        verifyNoMoreInteractions(iridologistRepository);
        verifyNoInteractions(iridologistMapper);
    }
}
