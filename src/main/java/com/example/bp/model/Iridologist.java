package com.example.bp.model;

import jakarta.persistence.*;

@Entity
public class Iridologist {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    protected Iridologist() {}

}