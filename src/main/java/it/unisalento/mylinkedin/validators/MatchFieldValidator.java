package it.unisalento.mylinkedin.validators;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchFieldValidator implements ConstraintValidator<MatchFieldConstraint, Object> {

    private String email;
    private String emailMatch;
    private String password;
    private String passwordMatch;

    @Override
    public void initialize(MatchFieldConstraint constraintAnnotation) {
        this.email = constraintAnnotation.email();
        this.emailMatch = constraintAnnotation.emailMatch();
        this.password = constraintAnnotation.password();
        this.passwordMatch = constraintAnnotation.passwordMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object emailValue = new BeanWrapperImpl(value).getPropertyValue(email);
        Object emailvalueMatch = new BeanWrapperImpl(value).getPropertyValue(emailMatch);
        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(password);
        Object passwordvalueMatch = new BeanWrapperImpl(value).getPropertyValue(passwordMatch);

        if (emailValue.equals(emailvalueMatch) && passwordValue.equals(passwordvalueMatch))
            return true;
        return false;
    }
}
