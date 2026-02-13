package com.example.bp.DAL.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Iridologist {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    protected Iridologist() {}

    public Iridologist(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}