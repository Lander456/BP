package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Iridologist extends HumanEntity {

    protected Iridologist() {}

    public Iridologist(String firstName, String lastName) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
    }
}