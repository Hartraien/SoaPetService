package ru.hartraien.petservice.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "pet_table",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "ownerId"}))
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_sequence")
    private Long id;
    @NotNull
    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;
    @Column(name = "birthdate")
    private LocalDate birthdate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "type")
    private PetType type;

    @NotNull
    @Column(name = "ownerId")
    private Long ownerId;

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    @NotNull
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(@NotNull Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", birthdate=" + birthdate +
                ", type=" + type.getTypeName() +
                ", ownerId=" + ownerId +
                '}';
    }
}
