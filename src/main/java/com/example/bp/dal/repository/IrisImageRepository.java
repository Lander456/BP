package com.example.bp.dal.repository;

import com.example.bp.dal.entity.IrisImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IrisImageRepository extends JpaRepository<IrisImage, Long> {
}
