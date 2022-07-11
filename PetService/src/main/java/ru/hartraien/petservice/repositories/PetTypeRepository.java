package ru.hartraien.petservice.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.hartraien.petservice.entities.PetType;

import java.util.Optional;

public interface PetTypeRepository extends JpaRepository<PetType, Long> {
    Optional<PetType> findByTypeNameIgnoreCase(String type);
}
