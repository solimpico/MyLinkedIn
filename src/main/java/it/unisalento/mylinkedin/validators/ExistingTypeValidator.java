package it.unisalento.mylinkedin.validators;

import it.unisalento.mylinkedin.dao.PostTypeRepository;
import it.unisalento.mylinkedin.domain.PostType;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ExistingTypeValidator implements ConstraintValidator<ExistingTypeConstraint, Object> {

    private String type;
    @Autowired
    PostTypeRepository postTypeRepository;

    @Override
    public void initialize(ExistingTypeConstraint constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object typeValue = new BeanWrapperImpl(value).getPropertyType(type);
        List<PostType> postTypeList = postTypeRepository.findAll();
        for (PostType postType : postTypeList){
            if(postType.getType().equalsIgnoreCase((String) typeValue)){
                return false;
            }
        }
        return true;
    }
}
