package ru.hartraien.petservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hartraien.petservice.entities.Pet;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByOwnerId(Long ownerId);
}
