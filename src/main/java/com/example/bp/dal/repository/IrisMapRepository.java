package com.example.bp.dal.repository;

import com.example.bp.dal.entity.IrisMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IrisMapRepository extends JpaRepository<IrisMap, Long> {
    List<IrisMap> findByIridologistId(Long iridologistId);
    Optional<IrisMap> findByImageUrl(String imageUrl);
    @Query("SELECT m FROM IrisMap m LEFT JOIN FETCH m.sectors WHERE m.id = :id")
    Optional<IrisMap> findByIdWithSectors(@Param("id") Long id);
    Boolean existsByImageUrl(String imageUrl);
}
