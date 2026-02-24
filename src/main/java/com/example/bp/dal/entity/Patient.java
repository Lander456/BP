package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Patient extends HumanEntity{

    private Byte age;

    private String birthNum;

    @ManyToOne(fetch=FetchType.LAZY)
    private Iridologist iridologist;

    private Boolean sex;

    protected Patient() {}

    public Patient(String firstName, String lastName, Byte age, String birthNum, Boolean sex) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        this.setAge(age);
        this.setBirthNum(birthNum);
        this.setSex(sex);
    }
}