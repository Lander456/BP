package com.example.bp.dal.repository;

import com.example.bp.dal.entity.Iridologist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IridologistRepository extends JpaRepository<Iridologist, Long> {

    boolean existsByUsername(String username);
    Optional<Iridologist> findByUsername(String username);

    List<Iridologist> findByFirstName(String firstName);
    List<Iridologist> findByLastName(String lastName);

}
