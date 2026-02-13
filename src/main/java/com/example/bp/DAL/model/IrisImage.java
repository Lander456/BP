package com.example.bp.DAL.model;

import jakarta.persistence.*;

@Entity
public class IrisImage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch=FetchType.LAZY)
    private Patient patient;
}