package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class IrisSector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private IrisMap irisMap;

    private String name;

    private Double startAngle;
    private Double endAngle;

    private double innerRadius;
    private double outerRadius;

    private String description;

    protected IrisSector() {}

    public IrisSector(String name, Double startAngle, Double endAngle) {
        this.name = name;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.innerRadius = 0.0;
        this.outerRadius = 1.0;
    }

}
