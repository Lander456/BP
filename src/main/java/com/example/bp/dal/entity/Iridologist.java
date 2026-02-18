package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Iridologist extends HumanEntity {

    protected Iridologist() {}

    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    String username;

    @Getter
    @Setter
    @Column(nullable = false)
    String password;

    public Iridologist(String firstName, String lastName, String username, String password) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        this.username = username;
        this.password = password;
    }
}