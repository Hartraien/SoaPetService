package ru.hartraien.petservice.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hartraien.petservice.entities.PetType;
import ru.hartraien.petservice.exceptions.TypeServiceException;
import ru.hartraien.petservice.repositories.PetTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PetTypeServiceImpl implements PetTypeService {

    private final PetTypeRepository petTypeRepository;

    private final Logger logger = LoggerFactory.getLogger(PetTypeServiceImpl.class);

    @Autowired
    public PetTypeServiceImpl(PetTypeRepository petTypeRepository) {
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    public List<PetType> getAllTypes() {
        return petTypeRepository.findAll();
    }

    @Override
    public PetType getTypeByName(String type) throws TypeServiceException {
        logger.debug(String.format("Seeking type in db '%s'", type));
        Optional<PetType> typeOptional = petTypeRepository.findByTypeNameIgnoreCase(type);
        if (typeOptional.isPresent()) {
            logger.debug("Returned pet type = " + typeOptional.get());
            return typeOptional.get();
        } else {
            String message = "No type of name " + type;
            logger.warn(message);
            throw new TypeServiceException(message);
        }
    }

    @Override
    public boolean contains(String type) {
        return petTypeRepository.findByTypeNameIgnoreCase(type).isPresent();
    }
}
