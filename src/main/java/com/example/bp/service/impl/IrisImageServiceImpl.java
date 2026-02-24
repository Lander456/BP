package com.example.bp.service.impl;

import com.example.bp.api.dto.IrisImageCreateDto;
import com.example.bp.api.dto.IrisImageDetailDto;
import com.example.bp.api.dto.IrisImageListDto;
import com.example.bp.api.mapper.IrisImageMapper;
import com.example.bp.dal.entity.IrisImage;
import com.example.bp.dal.entity.Patient;
import com.example.bp.dal.repository.IrisImageRepository;
import com.example.bp.dal.repository.PatientRepository;
import com.example.bp.service.FileStorageService;
import com.example.bp.service.IrisImageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IrisImageServiceImpl implements IrisImageService {

    private final FileStorageService fileStorageService;
    private final PatientRepository patientRepository;
    private final IrisImageMapper mapper;
    private final IrisImageRepository irisImageRepository;

    @Override
    public IrisImageDetailDto upload(MultipartFile file, IrisImageCreateDto metadata) {
        Patient patient = patientRepository.findById(metadata.patientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + metadata.patientId()));

        String savedFileName = fileStorageService.save(file);

        IrisImage irisImage = new IrisImage("/api/images/scans/" + savedFileName);
        irisImage.setPatient(patient);
        irisImage.setEyeSide(metadata.eyeSide());
        irisImage.setNote(metadata.notes());

        irisImage.setOriginalFileName(file.getOriginalFilename());
        irisImage.setContentType(file.getContentType());

        irisImage.setStoragePath("uploads/scans/" + savedFileName);
        irisImage.setImageUrl("/api/images/scans/" + savedFileName);

        IrisImage saved = irisImageRepository.save(irisImage);
        return mapper.toDetail(saved);
    }

    @Override
    public IrisImageDetailDto getById(Long id) {
        return irisImageRepository.findById(id)
                .map(mapper::toDetail)
                .orElseThrow(() -> new EntityNotFoundException("Iris image not found with ID: " + id));
    }

    @Override
    public List<IrisImageListDto> getByPatientId(Long patientId) {
        List<IrisImage> irisImages = irisImageRepository.findByPatientId(patientId);
        return mapper.toListDtoList(irisImages);
    }

    @Override
    public void delete(Long id) {
        irisImageRepository.deleteById(id);
    }
}
