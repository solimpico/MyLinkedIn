package it.unisalento.mylinkedin.iservicestest;

import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.Company;
import it.unisalento.mylinkedin.domain.Offeror;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.CompanyException;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IApplicantService;
import it.unisalento.mylinkedin.iservices.IOfferorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IOfferorServiceTest {

    @Autowired
    IOfferorService offerorService;
    @Mock
    IOfferorService offerorServiceMock;

    private Offeror offerorForFronted;
    private User frontendUser;
    private Offeror offeror;
    private Company company;
    private Company frontendCompany;

    @BeforeEach
    void initTestEnv(){
        this.company = new Company();
        this.company.setName("First Company");
        this.company.setAddress("test");
        this.company.setId(7);
        this.company.setOfferorList(null);
        this.company.setDescription("test");

        this.frontendCompany = new Company();
        this.frontendCompany.setName("CompanyTest");
        this.frontendCompany.setAddress("test");
        this.frontendCompany.setOfferorList(null);
        this.frontendCompany.setDescription("test");

        this.offeror = new Offeror();
        this.offeror.setId(1);
        this.offeror.setName("Mattia");
        this.offeror.setSurname("Olimpico");
        this.offeror.setAge(22);
        this.offeror.setBirthday("26/10/1998");
        this.offeror.setEmail("simone@studenti.unisalento.it");
        this.offeror.setPassword("password");
        this.offeror.setRegistered(true);
        this.offeror.setEnabling(true);
        this.offeror.setCompany(null);
        this.offeror.setMessageList(null);
        this.offeror.setPostList(null);
        this.offeror.setNotificationList(null);
        this.offeror.setProfileImage(null);

        this.frontendUser = new User();
        this.frontendUser.setName("Simone");
        this.frontendUser.setSurname("Olimpico");
        this.frontendUser.setAge(22);
        this.frontendUser.setBirthday("26/10/1998");
        this.frontendUser.setEmail("simone@studenti.unisalento.it");
        this.frontendUser.setPassword("password");

        this.offerorForFronted = new Offeror();
        this.offerorForFronted.setName("Mattia");
        this.offerorForFronted.setSurname("Olimpico");
        this.offerorForFronted.setAge(22);
        this.offerorForFronted.setBirthday("26/10/1998");
        this.offerorForFronted.setEmail("simone@studenti.unisalento.it");
        this.offerorForFronted.setPassword("password");
        this.offerorForFronted.setCompany(null);
        this.offerorForFronted.setMessageList(null);
        this.offerorForFronted.setPostList(null);
        this.offerorForFronted.setNotificationList(null);
        this.offerorForFronted.setProfileImage(null);

        try{
            when(offerorServiceMock.saveRegistrationtRequestOfferor(frontendUser)).thenReturn(offerorForFronted);
            when(offerorServiceMock.save(offeror)).thenReturn(offeror);
            when(offerorServiceMock.addCompany(offeror, frontendCompany)).thenReturn(frontendCompany);
        }
        catch (SavingUserException | CompanyException e){
            e.printStackTrace();
        }
    }

    @Test
    void saveRegistrationtRequestOfferorTest(){
        int id = 1000;
        boolean registered = true;
        boolean enabling = true;
        try{
            Offeror a = offerorServiceMock.saveRegistrationtRequestOfferor(frontendUser);
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
    void saveRegistrationRequesteOfferorThrowsExTest(){
        offerorForFronted.setEmail(null);
        Exception exp = assertThrows(SavingUserException.class, () -> {
            offerorService.saveRegistrationtRequestOfferor(frontendUser);
        });
        assertThat(exp).isNotNull();
    }

    @Test
    void findByUserIdTest(){
        try {
            assertThat(offerorService.findByUserId(this.offeror.getId())).isNotNull();
            assertThat(offerorService.findByUserId(this.offeror.getId()).getName()).isEqualTo(offeror.getName());
        }
        catch (UserNotFoundException e){
            e.printStackTrace();
        }
    }

    @Test
    void findByUserIdThrowsExTest(){
        Exception exp = assertThrows(UserNotFoundException.class, () -> {
            Offeror a = offerorService.findByUserId(0);
            System.out.println(a);
        });
        assertThat(exp).isNotNull();
    }

    @Test
    void saveTest(){
        int id = 100000;
        try{
            id = offerorServiceMock.save(offeror).getId();
        }
        catch (DataIntegrityViolationException e){
            e.printStackTrace();
        }
        assertThat(id).isEqualTo(offeror.getId());
    }

    @Test
    void saveThrowsExTest(){
        offeror.setEmail(null);
        Exception exp = assertThrows(DataIntegrityViolationException.class, () -> {
            offerorService.saveRegistrationtRequestOfferor(offeror);
        });
        assertThat(exp).isNotNull();
    }

    @Test
    void addCompanyTest(){
        int idComp = 100;
        try{
            idComp = offerorServiceMock.addCompany(offeror, frontendCompany).getId();
        }
        catch (CompanyException e){
            e.printStackTrace();
        }
        assertThat(idComp).isEqualTo(0);
    }

    @Test
    void addCompanyThrowsExTest(){
        Exception ex = assertThrows(CompanyException.class, () -> {
            offerorService.addCompany(null, frontendCompany);
        });
        assertThat(ex).isNotNull();
    }

    @Test
    void findCompanyByNameTest(){
        assertThat(offerorService.findCompanyByName(company.getName()).getName()).isEqualTo(company.getName());
    }

    @Test
    void deleteCompanyTest(){
        offerorServiceMock = mock(IOfferorService.class);
        try {
            doNothing().when(offerorServiceMock).deleteCompany(isA(Integer.class));
            offerorServiceMock.deleteCompany(company.getId());
            verify(offerorServiceMock, times(1)).deleteCompany(company.getId());
        }
        catch (CompanyException e){}
    }

    @Test
    void findOfferorRegistrationRequestTest(){
        assertThat(offerorService.findOfferorRegistrationRequest()).isNotNull();
    }
}
