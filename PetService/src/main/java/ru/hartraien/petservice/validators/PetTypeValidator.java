package ru.hartraien.petservice.validators;

import org.springframework.beans.factory.annotation.Autowired;
import ru.hartraien.petservice.Service.PetTypeService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PetTypeValidator implements ConstraintValidator<PetTypeValid, String> {

    private final PetTypeService petTypeService;

    @Autowired
    public PetTypeValidator(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    @Override
    public boolean isValid(String type, ConstraintValidatorContext constraintValidatorContext) {
        return type == null || petTypeService.contains(type);
    }
}
