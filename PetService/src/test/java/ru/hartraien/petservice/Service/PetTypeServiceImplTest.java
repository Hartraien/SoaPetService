package ru.hartraien.petservice.Service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.hartraien.petservice.entities.PetType;
import ru.hartraien.petservice.exceptions.TypeServiceException;
import ru.hartraien.petservice.repositories.PetTypeRepository;

import java.util.Optional;

class PetTypeServiceImplTest {

    private PetTypeRepository petTypeRepository;
    private PetTypeService petTypeService;

    @BeforeEach
    void init() {
        petTypeRepository = Mockito.mock(PetTypeRepository.class);
        petTypeService = new PetTypeServiceImpl(petTypeRepository);
    }

    @Test
    void getTypeByName_Valid() {
        String typeName = "typeName";
        PetType petType = new PetType(typeName);
        Mockito.when(petTypeRepository.findByTypeNameIgnoreCase(typeName)).thenReturn(Optional.of(petType));

        try {
            PetType typeByName = petTypeService.getTypeByName(typeName);
            Assertions.assertSame(petType, typeByName);
        } catch (TypeServiceException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getTypeByName_Invalid() {
        String typeName = "typeName";
        PetType petType = new PetType(typeName);
        Mockito.when(petTypeRepository.findByTypeNameIgnoreCase(typeName)).thenReturn(Optional.empty());

        Assertions.assertThrows(TypeServiceException.class, () -> petTypeService.getTypeByName(typeName));
    }
}