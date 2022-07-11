package ru.hartraien.petservice.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {
    String message() default "Should be null or not blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
