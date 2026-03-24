package com.example.bp.dal.repository;

import com.example.bp.api.dto.IrisImageCreateDto;
import com.example.bp.api.dto.IrisImageDetailDto;
import com.example.bp.dal.entity.IrisImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IrisImageRepository extends JpaRepository<IrisImage, Long> {
    List<IrisImage> findByPatient_Id(Long patientId);

    List<IrisImage> findByUploadedAt(LocalDateTime date);

    List<IrisImage> findByUploadedAtBefore(LocalDateTime date);

    List<IrisImage> findByUploadedAtAfter(LocalDateTime date);

    List<IrisImage> findByUploadedAtBetween(LocalDateTime dateAfter, LocalDateTime dateBefore);
}
