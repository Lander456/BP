package com.example.bp.DAL.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class IrisImage extends ImageEntity{

    @ManyToOne(fetch=FetchType.LAZY)
    private Patient patient;

    protected IrisImage() {};

    public IrisImage(String imageUrl) {
        super.setImageUrl(imageUrl);
    }
}