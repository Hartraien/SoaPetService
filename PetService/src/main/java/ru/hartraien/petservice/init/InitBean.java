package ru.hartraien.petservice.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.hartraien.petservice.entities.PetType;
import ru.hartraien.petservice.repositories.PetTypeRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InitBean {
    private final PetTypeRepository petTypeRepository;
    private final PetTypesHolder petTypesHolder;

    @Autowired
    public InitBean(PetTypeRepository petTypeRepository, PetTypesHolder petTypesHolder) {
        this.petTypeRepository = petTypeRepository;
        this.petTypesHolder = petTypesHolder;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onStartup(ApplicationStartedEvent startedEvent) {
        Set<String> typeNames = petTypeRepository.findAll().stream()
                .map(PetType::getTypeName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        Set<String> newTypes = petTypesHolder.getTypes().stream().map(String::toLowerCase).collect(Collectors.toSet());
        newTypes.removeAll(typeNames);
        petTypeRepository.saveAll(newTypes.stream().map(PetType::new).collect(Collectors.toList()));
    }
}
