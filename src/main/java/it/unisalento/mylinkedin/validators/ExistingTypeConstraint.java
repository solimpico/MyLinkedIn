package it.unisalento.mylinkedin.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingTypeValidator.class)
@Target(ElementType.TYPE)
public @interface ExistingTypeConstraint {
    String type();

    String message() default "ERRORE: TYPE GIÀ ESISTENTE";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
