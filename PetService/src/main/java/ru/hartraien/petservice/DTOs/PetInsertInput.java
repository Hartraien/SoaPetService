package ru.hartraien.petservice.DTOs;

import org.springframework.validation.annotation.Validated;
import ru.hartraien.petservice.entities.Sex;
import ru.hartraien.petservice.validators.PetTypeValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Validated
public class PetInsertInput {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private LocalDate birthdate;

    @NotNull
    private Sex sex;
    @NotNull
    @NotBlank
    @PetTypeValid(message = "Unknown pet type")
    private String type;

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

    @NotNull
    public String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
