package com.example.bp.dal.entity;

import com.example.bp.common.Sex;
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

    private Sex sex;

    protected Patient() {}

    public Patient(String firstName, String lastName, Byte age, String birthNum, Sex sex) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        this.setAge(age);
        this.setBirthNum(birthNum);
        this.setSex(sex);
    }
}