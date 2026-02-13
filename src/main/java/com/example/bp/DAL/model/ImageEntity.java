package com.example.bp.DAL.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class ImageEntity{

    @Id
    @GeneratedValue
    @Getter
    private Long Id;

    @Getter
    @Setter
    private String imageUrl;

}
