package ru.hartraien.petservice.Service;

import ru.hartraien.petservice.DTOs.PetInsertInput;
import ru.hartraien.petservice.DTOs.PetUpdateInput;
import ru.hartraien.petservice.entities.Pet;
import ru.hartraien.petservice.exceptions.PetServiceException;

import java.util.List;

public interface PetService {
    List<Pet> findAllPetsByOwnerId(Long userId);

    Pet getPetInfo(Long petId, Long ownerId) throws PetServiceException;

    void addPetForUser(PetInsertInput petInsertInput, Long userId) throws PetServiceException;

    void updatePetForUser(PetUpdateInput petUpdateInput, Long userId) throws PetServiceException;

    void deletePetForUser(Long petId, Long userId) throws PetServiceException;
}
