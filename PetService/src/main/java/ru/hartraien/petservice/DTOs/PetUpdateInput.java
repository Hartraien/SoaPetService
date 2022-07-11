package ru.hartraien.petservice.DTOs;

import org.springframework.validation.annotation.Validated;
import ru.hartraien.petservice.entities.Sex;
import ru.hartraien.petservice.validators.NullOrNotBlank;
import ru.hartraien.petservice.validators.PetTypeValid;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Validated
public class PetUpdateInput {
    @NotNull
    @Min(value = 1, message = "Only positive ids are allowed")
    private Long id;
    @NullOrNotBlank
    private String name;
    private LocalDate birthdate;

    private Sex sex;
    @NullOrNotBlank
    @PetTypeValid(message = "Unknown pet type")
    private String type;

    public PetUpdateInput() {
    }

    @NotNull
    public Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
