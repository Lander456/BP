package com.example.bp.dal.repository;

import com.example.bp.dal.entity.IrisMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IrisMapRepository extends JpaRepository<IrisMap, Long> {
}
