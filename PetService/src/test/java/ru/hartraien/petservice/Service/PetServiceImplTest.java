package ru.hartraien.petservice.Service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import ru.hartraien.petservice.DTOs.PetInsertInput;
import ru.hartraien.petservice.DTOs.PetUpdateInput;
import ru.hartraien.petservice.entities.Pet;
import ru.hartraien.petservice.entities.PetType;
import ru.hartraien.petservice.entities.Sex;
import ru.hartraien.petservice.exceptions.PetServiceException;
import ru.hartraien.petservice.exceptions.TypeServiceException;
import ru.hartraien.petservice.repositories.PetRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class PetServiceImplTest {

    private PetRepository petRepository;
    private PetTypeService petTypeService;

    private PetService petService;

    private final List<PetType> petTypes = generateRandomPetTypes();

    private List<PetType> generateRandomPetTypes() {
        PetType cat = new PetType();
        cat.setId(1L);
        cat.setTypeName("cat");
        return List.of(cat);
    }

    @BeforeEach
    void init() {
        petRepository = Mockito.mock(PetRepository.class);
        petTypeService = Mockito.mock(PetTypeService.class);
        petService = new PetServiceImpl(petRepository, petTypeService);
    }

    @Test
    void findAllPetsByOwnerId_Existing() {
        List<Pet> pets = new ArrayList<>();
        Long ownerId = 1L;
        fillPets(pets, ownerId);
        Mockito.when(petRepository.findAllByOwnerId(ownerId)).thenReturn(pets);

        Assertions.assertEquals(pets, petService.findAllPetsByOwnerId(ownerId));
    }

    private void fillPets(List<Pet> pets, Long ownerId) {
        int count = ThreadLocalRandom.current().nextInt();
        for (int i = 0; i < count; i++) {
            pets.add(generatePet(i + 1, ownerId));
        }
    }

    private Pet generatePet(int id, Long ownerId) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Pet pet = new Pet();
        pet.setId((long) id);
        pet.setBirthdate(LocalDate.now());
        pet.setSex(random.nextInt(0, 1) == 1 ? Sex.MALE : Sex.FEMALE);
        pet.setType(petTypes.get(random.nextInt(0, petTypes.size())));
        pet.setName(produceRandomString(10));
        pet.setOwnerId(ownerId);
        return pet;
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

    @Test
    void findAllPetsByOwnerId_NonExistent() {
        List<Pet> pets = new ArrayList<>();
        Long ownerId = 1L;
        fillPets(pets, ownerId);
        Mockito.when(petRepository.findAllByOwnerId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        Assertions.assertNotEquals(pets, petService.findAllPetsByOwnerId(ownerId));
    }

    @Test
    void getPetInfo_Valid() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        Mockito.when(petRepository.findById((long) id)).thenReturn(Optional.of(pet));

        try {
            Pet petInfo = petService.getPetInfo((long) id, ownerId);
            Assertions.assertSame(pet, petInfo);
        } catch (PetServiceException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getPetInfo_wrongId() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        Mockito.when(petRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            Pet petInfo = petService.getPetInfo((long) id, ownerId);
            Assertions.fail("returned empty pet");
        } catch (PetServiceException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("no pet"));
        }
    }

    @Test
    void getPetInfo_wrongOwnerId() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        Mockito.when(petRepository.findById((long) id)).thenReturn(Optional.of(pet));

        try {
            Pet petInfo = petService.getPetInfo((long) id, ownerId + 1);
            Assertions.fail("Returned unowned pet");
        } catch (PetServiceException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("does not own"));
        }
    }

    @Test
    void addPetForUser_valid() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName(pet.getName());
        petInsertInput.setBirthdate(pet.getBirthdate());
        petInsertInput.setSex(pet.getSex());
        petInsertInput.setType(pet.getType().getTypeName());

        try {
            Mockito.when(petTypeService.getTypeByName(pet.getType().getTypeName())).thenReturn(pet.getType());
        } catch (TypeServiceException e) {
            Assertions.fail("Failed to mock");
        }

        try {
            petService.addPetForUser(petInsertInput, ownerId);
        } catch (PetServiceException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void addPetForUser_invalid() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        PetInsertInput petInsertInput = new PetInsertInput();
        petInsertInput.setName(pet.getName());
        petInsertInput.setBirthdate(pet.getBirthdate());
        petInsertInput.setSex(pet.getSex());
        petInsertInput.setType(pet.getType().getTypeName());

        try {
            Mockito.when(petTypeService.getTypeByName(pet.getType().getTypeName())).thenReturn(pet.getType());
        } catch (TypeServiceException e) {
            Assertions.fail("Failed to mock");
        }

        String pet_already_exists = "pet already exists";
        Mockito.doThrow(new DataIntegrityViolationException(pet_already_exists)).when(petRepository).save(Mockito.any());

        try {
            petService.addPetForUser(petInsertInput, ownerId);
            Assertions.fail("Added existing pet");
        } catch (PetServiceException e) {
            Assertions.assertTrue(e.getCause().getMessage().contains(pet_already_exists));
        }
    }

    @Test
    void updatePetForUser_Valid() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        PetUpdateInput petUpdateInput = new PetUpdateInput();
        petUpdateInput.setId((long) id);
        petUpdateInput.setName(pet.getName());
        petUpdateInput.setBirthdate(pet.getBirthdate());
        petUpdateInput.setSex(pet.getSex());
        petUpdateInput.setType(pet.getType().getTypeName());


        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));


        try {
            petService.updatePetForUser(petUpdateInput, ownerId);
        } catch (PetServiceException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void updatePetForUser_NonExistent() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        PetUpdateInput petUpdateInput = new PetUpdateInput();
        petUpdateInput.setId((long) id);
        petUpdateInput.setName(pet.getName());
        petUpdateInput.setBirthdate(pet.getBirthdate());
        petUpdateInput.setSex(pet.getSex());
        petUpdateInput.setType(pet.getType().getTypeName());


        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Optional.empty());


        try {
            petService.updatePetForUser(petUpdateInput, ownerId);
            Assertions.fail("modified nonexistent pet");
        } catch (PetServiceException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("no pet"));
        }
    }

    @Test
    void updatePetForUser_WrongOwner() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        PetUpdateInput petUpdateInput = new PetUpdateInput();
        petUpdateInput.setId((long) id);
        petUpdateInput.setName(pet.getName());
        petUpdateInput.setBirthdate(pet.getBirthdate());
        petUpdateInput.setSex(pet.getSex());
        petUpdateInput.setType(pet.getType().getTypeName());


        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));


        try {
            petService.updatePetForUser(petUpdateInput, ownerId + 1);
            Assertions.fail("modified not owned pet");
        } catch (PetServiceException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("does not own"));
        }
    }

    @Test
    void deletePetForUser_Valid() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        Mockito.when(petRepository.findById((long) id)).thenReturn(Optional.of(pet));

        try {
            petService.deletePetForUser((long) id, ownerId);

        } catch (PetServiceException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void deletePetForUser_NonExistent() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        Mockito.when(petRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            petService.deletePetForUser((long) id, ownerId);
            Assertions.fail("Deleted empty pet");
        } catch (PetServiceException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("no pet"));
        }
    }

    @Test
    void deletePetForUser_wrongOwnerId() {
        int id = 1;
        long ownerId = 1;
        Pet pet = generatePet(id, ownerId);

        Mockito.when(petRepository.findById((long) id)).thenReturn(Optional.of(pet));

        try {
            petService.deletePetForUser((long) id, ownerId + 1);
            Assertions.fail("Deleted unowned pet");
        } catch (PetServiceException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("does not own"));
        }
    }
}