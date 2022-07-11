package ru.hartraien.petservice.DTOs;

import ru.hartraien.petservice.entities.Pet;
import ru.hartraien.petservice.entities.Sex;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class PetFullInfo {
    private Long id;

    private String name;

    private LocalDate birthdate;

    private Sex sex;

    private String type;

    public PetFullInfo() {
    }

    public PetFullInfo(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.birthdate = pet.getBirthdate();
        this.sex = pet.getSex();
        this.type = pet.getType().getTypeName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @NotNull
    public String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }
}
