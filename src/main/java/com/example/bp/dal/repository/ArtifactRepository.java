package com.example.bp.dal.repository;

import com.example.bp.dal.entity.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
    List<Artifact> findByName(String name);
    Optional<Artifact> findByLabelCode(String labelCode);
}
