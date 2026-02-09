package com.example.bp.model;

import jakarta.persistence.*;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private Short age;
    private Long birthNum;

    @ManyToOne(fetch=FetchType.LAZY)
    private Iridologist iridologist;

    protected Patient() {}

}