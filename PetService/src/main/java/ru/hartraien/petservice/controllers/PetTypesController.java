package ru.hartraien.petservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hartraien.petservice.DTOs.PetTypesList;
import ru.hartraien.petservice.Service.PetTypeService;

@RestController
@RequestMapping("/pet-types")
public class PetTypesController {
    public final PetTypeService petTypeService;

    @Autowired
    public PetTypesController(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    @GetMapping
    public ResponseEntity<PetTypesList> getAllPetTypes(@RequestHeader("id") Long userId) {
        return ResponseEntity.ok(new PetTypesList(petTypeService.getAllTypes()));
    }
}
