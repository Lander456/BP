package com.example.bp.repository;

import com.example.bp.model.Iridologist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IridologistRepository extends JpaRepository<Iridologist, Long> {
}
