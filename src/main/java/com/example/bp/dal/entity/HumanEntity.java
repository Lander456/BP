package com.example.bp.dal.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode
public abstract class HumanEntity{
    @Id
    @GeneratedValue
    private Long Id;

    private String firstName;

    private String lastName;
}
