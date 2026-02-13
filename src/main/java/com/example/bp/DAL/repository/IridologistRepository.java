package com.example.bp.DAL.repository;

import com.example.bp.DAL.model.Iridologist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IridologistRepository extends JpaRepository<Iridologist, Long> {

    List<Iridologist> findByFirstName(String firstName);
    List<Iridologist> findByLastName(String lastName);

}
