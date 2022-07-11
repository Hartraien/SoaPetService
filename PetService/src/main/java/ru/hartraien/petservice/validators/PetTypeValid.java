package ru.hartraien.petservice.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PetTypeValidator.class)
@Documented
public @interface PetTypeValid {
    /**
     * Validation message
     */
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
