package com.example.bp.dal.repository;

import com.example.bp.dal.entity.IrisSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IrisSectorRepository extends JpaRepository<IrisSector, Long> {
    List<IrisSector> findByIrisMapId(Long mapId);
    Optional<IrisSector> findByIrisMapIdAndName(Long mapId, String name);
}
