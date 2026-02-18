package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class IrisImage extends ImageEntity{

    @Getter
    @Setter
    @ManyToOne(fetch=FetchType.LAZY)
    private Patient patient;

    protected IrisImage() {}

    public IrisImage(String imageUrl) {
        super.setImageUrl(imageUrl);
    }
}