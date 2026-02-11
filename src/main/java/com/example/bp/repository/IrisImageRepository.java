package com.example.bp.repository;

import com.example.bp.model.IrisImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IrisImageRepository extends JpaRepository<IrisImage, Long> {
}
