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
public class IrisImage extends ImageEntity{

    @ManyToOne(fetch=FetchType.LAZY)
    private Patient patient;

    private String diagnosis;

    private String eyeSide;

    private String note;

    @OneToMany(mappedBy = "irisImage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IrisFinding> findings = new ArrayList<>();

    protected IrisImage() {}

    public IrisImage(String imageUrl) {
        super.setImageUrl(imageUrl);
    }

    public void addFinding(IrisFinding finding) {
        findings.add(finding);
        finding.setIrisImage(this);
    }
}