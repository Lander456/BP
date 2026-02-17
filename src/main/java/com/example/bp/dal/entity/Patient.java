package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Patient extends HumanEntity{

    @Getter
    @Setter
    private Byte age;

    @Getter
    @Setter
    private String birthNum;

    @Getter
    @Setter
    @ManyToOne(fetch=FetchType.LAZY)
    private Iridologist iridologist;

    protected Patient() {}

    public Patient(String firstName, String lastName, Byte age, String birthNum) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        this.setAge(age);
        this.setBirthNum(birthNum);
    }
}