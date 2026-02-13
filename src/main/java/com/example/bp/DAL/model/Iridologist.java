package com.example.bp.DAL.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Iridologist extends HumanEntity {

    protected Iridologist() {}

    public Iridologist(String firstName, String lastName) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
    }
}