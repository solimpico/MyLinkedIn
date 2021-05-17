package it.unisalento.mylinkedin.iservicestest;

import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IApplicantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IApplicantServiceTest {

    @Autowired
    IApplicantService applicantService;
    @Mock
    IApplicantService applicantServiceMock;

    private Applicant applicantForFronted;
    private User frontendUser;
    private Applicant applicant;


    @BeforeEach
    void initTestEnv(){
        this.applicant = new Applicant();
        this.applicant.setId(2);
        this.applicant.setName("Simone");
        this.applicant.setSurname("Olimpico");
        this.applicant.setAge(22);
        this.applicant.setBirthday("26/10/1998");
        this.applicant.setEmail("simone@studenti.unisalento.it");
        this.applicant.setPassword("password");
        this.applicant.setRegistered(true);
        this.applicant.setEnabling(true);
        this.applicant.setSkilApplicantList(null);
        this.applicant.setMessageList(null);
        this.applicant.setPostList(null);
        this.applicant.setNotificationList(null);
        this.applicant.setProfileImage(null);

        this.frontendUser = new User();
        this.frontendUser.setName("Simone");
        this.frontendUser.setSurname("Olimpico");
        this.frontendUser.setAge(22);
        this.frontendUser.setBirthday("26/10/1998");
        this.frontendUser.setEmail("simone@studenti.unisalento.it");
        this.frontendUser.setPassword("password");

        this.applicantForFronted = new Applicant();
        this.applicantForFronted.setName("Simone");
        this.applicantForFronted.setSurname("Olimpico");
        this.applicantForFronted.setAge(22);
        this.applicantForFronted.setBirthday("26/10/1998");
        this.applicantForFronted.setEmail("simone@studenti.unisalento.it");
        this.applicantForFronted.setPassword("password");
        this.applicantForFronted.setSkilApplicantList(null);
        this.applicantForFronted.setMessageList(null);
        this.applicantForFronted.setPostList(null);
        this.applicantForFronted.setNotificationList(null);
        this.applicantForFronted.setProfileImage(null);

        try{
            when(applicantServiceMock.saveRegistrationtRequestApplicant(frontendUser)).thenReturn(applicantForFronted);
            when(applicantServiceMock.save(applicant)).thenReturn(applicant);
        }
        catch (SavingUserException e){
            e.printStackTrace();
        }
    }

    @Test
    void saveRegistrationtRequestApplicantTest(){
        int id = 1000;
        boolean registered = true;
        boolean enabling = true;
        try{
            Applicant a = applicantServiceMock.saveRegistrationtRequestApplicant(frontendUser);
            id = a.getId();
            registered = a.isRegistered();
            enabling = a.isEnabling();
        }
            catch (SavingUserException e){
        }
        assertThat(id).isEqualTo(0);
        assertThat(registered).isEqualTo(false);
        assertThat(enabling).isEqualTo(false);
    }

    @Test
    void saveRegistrationRequesteApplicantThrowsExTest(){
        applicantForFronted.setEmail(null);
        Exception exp = assertThrows(SavingUserException.class, () -> {
            applicantService.saveRegistrationtRequestApplicant(frontendUser);
        });
        assertThat(exp).isNotNull();
    }

    @Test
    void findByUserIdTest(){
        try {
            assertThat(applicantService.findByUserId(this.applicant.getId())).isNotNull();
            assertThat(applicantService.findByUserId(this.applicant.getId()).getName()).isEqualTo(applicant.getName());
        }
        catch (UserNotFoundException e){
            e.printStackTrace();
        }
    }

    @Test
    void findByUserIdThrowsExTest(){
        Exception exp = assertThrows(UserNotFoundException.class, () -> {
            Applicant a = applicantService.findByUserId(0);
            System.out.println(a);
        });
        assertThat(exp).isNotNull();
    }

    @Test
    void saveTest(){
        int id = 100000;
        try{
            id = applicantServiceMock.save(applicant).getId();
        }
        catch (DataIntegrityViolationException e){
            e.printStackTrace();
        }
        assertThat(id).isEqualTo(applicant.getId());
    }

    @Test
    void saveThrowsExTest(){
        applicant.setEmail(null);
        Exception exp = assertThrows(DataIntegrityViolationException.class, () -> {
            applicantService.saveRegistrationtRequestApplicant(applicant);
        });
        assertThat(exp).isNotNull();
    }

}
