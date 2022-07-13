package ru.hartraien.petservice.DTOs;

import ru.hartraien.petservice.entities.PetType;

import java.util.List;
import java.util.stream.Collectors;

public class PetTypesList {
    private List<String> types;

    public PetTypesList() {
    }

    public PetTypesList(List<PetType> allTypes) {
        types = allTypes.stream().map(PetType::getTypeName).collect(Collectors.toList());
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
