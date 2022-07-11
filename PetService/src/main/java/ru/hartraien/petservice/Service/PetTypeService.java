package ru.hartraien.petservice.Service;

import ru.hartraien.petservice.entities.PetType;
import ru.hartraien.petservice.exceptions.TypeServiceException;

import java.util.List;

public interface PetTypeService {
    List<PetType> getAllTypes();

    PetType getTypeByName(String type) throws TypeServiceException;

    boolean contains(String type);
}
