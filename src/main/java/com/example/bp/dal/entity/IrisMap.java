package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class IrisMap extends ImageEntity {

    @ManyToOne(fetch=FetchType.LAZY)
    private Iridologist iridologist;

    @OneToMany(mappedBy = "irisMap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IrisSector> sectors = new ArrayList<>();

    protected IrisMap() {}

    public IrisMap(String imageUrl) {
        super.setImageUrl(imageUrl);
    }

    public void addSector(IrisSector sector) {
        sectors.add(sector);
        sector.setIrisMap(this);
    }

    public void clearSectors() {
        this.sectors.clear();
    }
}