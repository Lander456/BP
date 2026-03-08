package com.example.bp.dal.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Artifact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(unique = true, nullable = false)
    private String labelCode;

    protected Artifact() {}

    public Artifact(String name, String description, String labelCode) {
        this.name = name;
        this.description = description;
        this.labelCode = (labelCode != null) ? labelCode : generatedUserCode(name);
    }

    private @NonNull String generatedUserCode(@NonNull String labelName) {
        return "USR-" + labelName.toUpperCase().replace(" ", "_") + "-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
