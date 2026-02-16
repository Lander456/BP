package com.example.bp.DAL.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class IrisMap extends ImageEntity {

    @Getter
    @Setter
    @ManyToOne(fetch=FetchType.LAZY)
    private Iridologist iridologist;

    protected IrisMap() {}

    public IrisMap(String imageUrl) {
        super.setImageUrl(imageUrl);
    }
}