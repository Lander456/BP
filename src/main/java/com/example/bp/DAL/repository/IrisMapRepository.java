package com.example.bp.DAL.repository;

import com.example.bp.DAL.model.IrisImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IrisMapRepository extends JpaRepository<IrisImage, Long> {
}
