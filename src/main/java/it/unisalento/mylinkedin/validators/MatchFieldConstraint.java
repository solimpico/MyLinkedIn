package it.unisalento.mylinkedin.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchFieldValidator.class)
@Target(ElementType.TYPE)
public @interface MatchFieldConstraint {

    String email();
    String emailMatch();
    String password();
    String passwordMatch();

    String message() default "ERRORE: I CAMPI DEVONO ESSERE UGUALI";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
