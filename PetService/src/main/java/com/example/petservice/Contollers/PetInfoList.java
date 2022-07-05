package com.example.petservice.Contollers;

import java.util.List;

public class PetInfoList {
    private List<PetInfo> pets;

    public PetInfoList() {
    }

    public List<PetInfo> getPets() {
        return pets;
    }

    public void setPets(List<PetInfo> pets) {
        this.pets = pets;
    }
}
