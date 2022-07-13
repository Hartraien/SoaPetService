package ru.hartraien.petservice.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import ru.hartraien.petservice.DTOs.PetTypesList;
import ru.hartraien.petservice.Service.PetTypeService;
import ru.hartraien.petservice.entities.PetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class PetTypesControllerTest {

    private PetTypeService petTypeService;
    private PetTypesController petTypesController;

    @BeforeEach
    void init() {
        petTypeService = Mockito.mock(PetTypeService.class);
        petTypesController = new PetTypesController(petTypeService);
    }

    @Test
    void getAllPetTypes() {
        List<PetType> petTypeList = generatePetTypes();
        Long id = 1L;

        Mockito.when(petTypeService.getAllTypes()).thenReturn(petTypeList);

        ResponseEntity<PetTypesList> response = petTypesController.getAllPetTypes(id);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(new PetTypesList(petTypeList).getTypes(), response.getBody().getTypes());
    }

    private List<PetType> generatePetTypes() {
        int count = ThreadLocalRandom.current().nextInt(1, 10);
        List<PetType> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(new PetType(produceRandomString(10)));
        }
        return result;
    }

    private String produceRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = ThreadLocalRandom.current();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}