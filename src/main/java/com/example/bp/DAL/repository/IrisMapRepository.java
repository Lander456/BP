package com.example.bp.DAL.repository;

import com.example.bp.DAL.model.IrisMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IrisMapRepository extends JpaRepository<IrisMap, Long> {
}
