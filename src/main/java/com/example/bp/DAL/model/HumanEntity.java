package com.example.bp.DAL.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EqualsAndHashCode
public abstract class HumanEntity{
    @Id
    @GeneratedValue
    @Getter
    private Long Id;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;
}
