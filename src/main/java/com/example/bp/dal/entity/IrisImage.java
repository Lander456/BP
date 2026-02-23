package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class IrisImage extends ImageEntity{
    //TODO rework IrisImage entity

    @ManyToOne(fetch=FetchType.LAZY)
    private Patient patient;

    private String diagnosis;

    private String note;

    protected IrisImage() {}

    public IrisImage(String imageUrl) {
        super.setImageUrl(imageUrl);
    }
}