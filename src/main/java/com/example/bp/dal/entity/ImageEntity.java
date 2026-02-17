package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EqualsAndHashCode
public abstract class ImageEntity{

    @Id
    @GeneratedValue
    @Getter
    private Long Id;

    @Getter
    @Setter
    @Column(unique = true)
    private String imageUrl;

}
