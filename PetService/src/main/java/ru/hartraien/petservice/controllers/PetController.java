package ru.hartraien.petservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hartraien.petservice.DTOs.*;
import ru.hartraien.petservice.Service.PetService;
import ru.hartraien.petservice.entities.Pet;
import ru.hartraien.petservice.exceptions.PetServiceException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<PetInfoList> getAllPets(@RequestHeader("id") Long userId) {
        List<Pet> allUsersPets = petService.findAllPetsByOwnerId(userId);
        PetInfoList petInfoList = new PetInfoList();
        petInfoList.setPets(allUsersPets.stream()
                .map(pet -> new PetInfo(pet.getId(), pet.getName()))
                .collect(Collectors.toList()));
        return ResponseEntity.ok(petInfoList);
    }

    @GetMapping("/{pet_id}")
    public ResponseEntity<PetFullInfo> getFullPetInfo(@RequestHeader("id") Long userId
            , @PathVariable("pet_id") @Min(value = 1, message = "Only positive pet ids are allowed") Long petId) throws PetServiceException {
        Pet pet = petService.getPetInfo(petId, userId);
        PetFullInfo petFullInfo = new PetFullInfo(pet);
        return ResponseEntity.ok(petFullInfo);
    }

    @PostMapping
    public ResponseEntity<Void> addPet(@RequestHeader("id") Long userId
            , @Valid @RequestBody PetInsertInput petInsertInput) throws PetServiceException {
        petService.addPetForUser(petInsertInput, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<PetFullInfo> updatePet(@RequestHeader("id") Long userId
            , @Valid @RequestBody PetUpdateInput petUpdateInput) throws PetServiceException {
        petService.updatePetForUser(petUpdateInput, userId);
        return getFullPetInfo(userId, petUpdateInput.getId());
    }

    @DeleteMapping("/{pet_id}")
    public ResponseEntity<Void> deletePet(@RequestHeader("id") Long userId
            , @PathVariable("pet_id") @Min(value = 1, message = "Only positive ids are allowed") Long petId) throws PetServiceException {
        petService.deletePetForUser(petId, userId);
        return ResponseEntity.ok().build();
    }
}
