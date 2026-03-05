package com.example.bp.dal.repository;

import com.example.bp.dal.entity.IrisImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IrisImageRepository extends JpaRepository<IrisImage, Long> {
    List<IrisImage> findByPatient_Id(Long patientId);
}
