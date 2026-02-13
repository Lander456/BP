package com.example.bp.DAL.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class IrisMap extends ImageEntity {

    @ManyToOne(fetch=FetchType.LAZY)
    private Iridologist iridologist;

    protected IrisMap() {}

    public IrisMap(String imageUrl) {
        super.setImageUrl(imageUrl);
    }
}