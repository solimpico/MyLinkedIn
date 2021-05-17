package it.unisalento.mylinkedin.iservicestest;

import it.unisalento.mylinkedin.domain.Administrator;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IAdminServiceTest {

    @Mock
    private IAdminService adminServiceMock;
    @Autowired
    private IAdminService adminService;

    private User frontendUser;
    private Administrator administrator;
    private Administrator administratorForFrontend;

    @BeforeEach
    void initTestEnv(){
        this.administrator = new Administrator();
        this.administrator.setId(1);
        this.administrator.setName("Mattia");
        this.administrator.setSurname("CognomeDiProva");
        this.administrator.setAge(22);
        this.administrator.setBirthday("26/10/1998");
        this.administrator.setEmail("prova@prova.it");
        this.administrator.setPassword("prova");
        this.administrator.setMessageList(null);
        this.administrator.setNotificationList(null);
        this.administrator.setPostList(null);
        this.administrator.setProfileImage(null);


        this.frontendUser = new User();
        this.frontendUser.setName("NomaDiProva");
        this.frontendUser.setSurname("CognomeDiProva");
        this.frontendUser.setAge(22);
        this.frontendUser.setBirthday("26/10/1998");
        this.frontendUser.setEmail("prova@prova.it");
        this.frontendUser.setPassword("prova");

        this.administratorForFrontend = new Administrator();
        this.administratorForFrontend.setName("Mattia");
        this.administratorForFrontend.setSurname("CognomeDiProva");
        this.administratorForFrontend.setAge(22);
        this.administratorForFrontend.setBirthday("26/10/1998");
        this.administratorForFrontend.setEmail("prova@prova.it");
        this.administratorForFrontend.setPassword("prova");
        this.administratorForFrontend.setMessageList(null);
        this.administratorForFrontend.setNotificationList(null);
        this.administratorForFrontend.setPostList(null);
        this.administratorForFrontend.setProfileImage(null);

        try{
            when(adminServiceMock.saveRegistrationRequestAdmin(frontendUser)).thenReturn(administratorForFrontend);
        } catch (SavingUserException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveRegistrationRequestAdminTest(){
        int id = 100000;
        try{
            id = adminServiceMock.saveRegistrationRequestAdmin(frontendUser).getId();
        }
        catch (SavingUserException e){
            e.printStackTrace();
        }
        assertThat(id).isEqualTo(0);
    }

    @Test
    void saveRegistrationRequesteAdminExTest(){
        frontendUser.setEmail(null);
        Exception exp = assertThrows(SavingUserException.class, () -> {
            adminService.saveRegistrationRequestAdmin(frontendUser);
        });
        assertThat(exp).isNotNull();
    }

    @Test
    void findByUserIdTest(){
        try {
            assertThat(adminService.findByUserId(this.administrator.getId())).isNotNull();
            assertThat(adminService.findByUserId(this.administrator.getId()).getName()).isEqualTo(administrator.getName());
        }
        catch (UserNotFoundException e){
            e.printStackTrace();
        }
    }

    @Test
    void findByUserIdExTest(){
        Exception exp = assertThrows(UserNotFoundException.class, () -> {
            adminService.findByUserId(0);
        });
        assertThat(exp).isNotNull();
    }

}
