package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Artifact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(unique = true, nullable = false)
    private String labelCode;

    protected Artifact() {}

    public Artifact(String name, String description, String labelCode) {
        this.name = name;
        this.description = description;
        this.labelCode = labelCode;
    }
}
