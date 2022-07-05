package com.example.petservice.Contollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @GetMapping
    public ResponseEntity<PetInfoList> getAllPets(){
        PetInfoList petInfoList = new PetInfoList();
        List<PetInfo> list = new ArrayList<>();
        list.add(new PetInfo("One"));
        list.add(new PetInfo("Two"));
        list.add(new PetInfo("Three"));
        petInfoList.setPets(list);
        return ResponseEntity.ok(petInfoList);
    }
}
