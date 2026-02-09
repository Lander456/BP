package com.example.bp.model;

import jakarta.persistence.*;

@Entity
public class IrisImage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    private Patient patient;
}