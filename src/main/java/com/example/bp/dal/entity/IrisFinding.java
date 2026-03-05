package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class IrisFinding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private IrisImage irisImage;

    @ManyToOne(fetch = FetchType.LAZY)
    private IrisSector irisSector;

    @ManyToOne(fetch = FetchType.LAZY)
    private Artifact artifact;

    private Double confidenceScore;

    private Boolean isAiGenerated;

    private Boolean isValidated;

    @Column(columnDefinition = "TEXT")
    private String geometryJson;

    protected IrisFinding() {}

    public IrisFinding(IrisImage irisImage, IrisSector irisSector, Artifact artifact, String geometryJson) {
        this.irisImage = irisImage;
        this.irisSector = irisSector;
        this.artifact = artifact;
        this.geometryJson = geometryJson;
        this.isAiGenerated = false;
        this.confidenceScore = 1.0;
        this.isValidated = true;
    }
}
