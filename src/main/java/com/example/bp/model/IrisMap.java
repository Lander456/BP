package com.example.bp.model;

import jakarta.persistence.*;

@Entity
public class IrisMap {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch=FetchType.LAZY)
    private Iridologist iridologist;
}