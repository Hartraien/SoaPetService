package ru.hartraien.petservice.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class PetType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_type_sequence")
    private Long id;

    @NotNull
    @NotBlank
    private String typeName;

    public PetType() {
    }

    public PetType(String name) {
        typeName = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String type) {
        this.typeName = type;
    }

    @Override
    public String toString() {
        return "PetType{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
