package ru.hartraien.petservice.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.hartraien.petservice.DTOs.PetInsertInput;
import ru.hartraien.petservice.DTOs.PetUpdateInput;
import ru.hartraien.petservice.entities.Pet;
import ru.hartraien.petservice.exceptions.PetServiceException;
import ru.hartraien.petservice.exceptions.TypeServiceException;
import ru.hartraien.petservice.repositories.PetRepository;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {
    private final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);
    private final PetRepository petRepository;

    private final PetTypeService petTypeService;

    @Autowired
    public PetServiceImpl(PetRepository petRepository, PetTypeService petTypeService) {
        this.petRepository = petRepository;
        this.petTypeService = petTypeService;
    }

    @Override
    public List<Pet> findAllPetsByOwnerId(Long userId) {
        return petRepository.findAllByOwnerId(userId);
    }

    @Override
    public Pet getPetInfo(Long petId, Long ownerId) throws PetServiceException {
        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isPresent() && Objects.equals(ownerId, petOptional.get().getOwnerId())) {
            return petOptional.get();
        } else {
            String innerMessage = petOptional.isEmpty() ? "No pet with id " + petId + " exists" : "User with id " + ownerId + " does not own pet with id " + petId;
            String outerMessage = "No pet with id " + petId + " exists";
            logger.warn(innerMessage);
            throw new PetServiceException(innerMessage, outerMessage);
        }
    }

    @Override
    public void addPetForUser(PetInsertInput petInsertInput, Long userId) throws PetServiceException {
        try {
            Pet pet = convertInputToPet(petInsertInput);
            pet.setOwnerId(userId);
            logger.debug("Successfully converted input to pet");
            logger.debug("Result pet = " + pet);
            petRepository.save(pet);
        } catch (DataIntegrityViolationException | TypeServiceException | ConstraintViolationException exception) {
            String message = exception.getMessage();
            logger.warn(message);
            throw new PetServiceException("Could not add pet for user", exception);
        }
    }

    private Pet convertInputToPet(PetInsertInput petInsertInput) throws TypeServiceException {
        Pet result = new Pet();
        result.setName(petInsertInput.getName());
        result.setBirthdate(petInsertInput.getBirthdate());
        result.setSex(petInsertInput.getSex());
        result.setType(petTypeService.getTypeByName(petInsertInput.getType()));
        return result;
    }

    @Override
    public void updatePetForUser(PetUpdateInput petUpdateInput, Long ownerId) throws PetServiceException {
        Long petId = petUpdateInput.getId();
        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isPresent() && Objects.equals(ownerId, petOptional.get().getOwnerId())) {
            Pet pet = petOptional.get();
            try {
                updatePetValues(pet, petUpdateInput);
            } catch (TypeServiceException e) {
                logger.warn(e.getMessage());
                throw new PetServiceException(e.getMessage(), e);
            }
            petRepository.save(pet);
        } else {
            String innerMessage = petOptional.isEmpty() ? "No pet with id " + petId + " exists" : "User with id " + ownerId + " does not own pet with id " + petId;
            String outerMessage = "No pet with id " + petId + " exists";
            logger.warn(innerMessage);
            throw new PetServiceException(innerMessage, outerMessage);
        }
    }

    private void updatePetValues(Pet pet, PetUpdateInput petUpdateInput) throws TypeServiceException {
        if (petUpdateInput.getName() != null)
            pet.setName(petUpdateInput.getName());
        if (petUpdateInput.getBirthdate() != null)
            pet.setBirthdate(petUpdateInput.getBirthdate());
        if (petUpdateInput.getSex() != null)
            pet.setSex(petUpdateInput.getSex());
        if (petUpdateInput.getType() != null)
            pet.setType(petTypeService.getTypeByName(petUpdateInput.getType()));
    }

    @Override
    public void deletePetForUser(Long petId, Long ownerId) throws PetServiceException {
        Optional<Pet> petOptional = petRepository.findById(petId);
        if (petOptional.isPresent() && Objects.equals(ownerId, petOptional.get().getOwnerId())) {
            petRepository.deleteById(petId);
        } else {
            String innerMessage = petOptional.isEmpty() ? "No pet with id " + petId + " exists" : "User with id " + ownerId + " does not own pet with id " + petId;
            String outerMessage = "No pet with id " + petId + " exists";
            logger.warn(innerMessage);
            throw new PetServiceException(innerMessage, outerMessage);
        }
    }
}
