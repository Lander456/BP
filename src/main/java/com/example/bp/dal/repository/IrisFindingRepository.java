package com.example.bp.dal.repository;

import com.example.bp.dal.entity.IrisFinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IrisFindingRepository extends JpaRepository<IrisFinding, Long> {
    List<IrisFinding> findByIrisImage_Id(Long imageId);
    List<IrisFinding> findByIsValidatedFalse();
    List<IrisFinding> findByArtifact_LabelCode(String labelCode);
}
