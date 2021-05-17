package it.unisalento.mylinkedin.iservicestest;

import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IApplicantService;
import it.unisalento.mylinkedin.iservices.IRequiredFieldsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IRequiredFieldServiceTest {

    @Autowired
    IRequiredFieldsService requiredFieldsService;

    private RequiredField requiredField;
    private int idType;

    @BeforeEach
    void initTestEnv(){
        this.idType = 1;

        this.requiredField = new RequiredField();
        this.requiredField.setId(1);
        this.requiredField.setRequiredField("Titolo");
        this.requiredField.setRequiredFieldPostTypeList(null);
    }

    @Test
    void getAllTest(){
        assertThat(requiredFieldsService.getAll()).isNotNull();
    }

    @Test
    void findByTypeTest(){
        assertThat(requiredFieldsService.findByType(idType)).isNotNull();
        assertThat(requiredFieldsService.findByType(idType).get(0).getRequiredField()).isEqualTo(requiredField.getRequiredField());
    }

}
