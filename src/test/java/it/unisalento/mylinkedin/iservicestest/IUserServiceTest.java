package it.unisalento.mylinkedin.iservicestest;

import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.ProfileImage;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.ImageNotFoundException;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IApplicantService;
import it.unisalento.mylinkedin.iservices.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IUserServiceTest {

    @Autowired
    IUserService userService;
    @Mock
    IUserService userServiceMock;

    private User user;
    private User frontedUser;
    private ProfileImage profileImage;

    @BeforeEach
    void initTestEnv(){
        this.profileImage = new ProfileImage();
        this.profileImage.setId(0);
        this.profileImage.setUser(this.user);
        this.profileImage.setProfilePicturePath("path/di/test");
        this.profileImage.setDescription("test");

        this.user = new User();
        this.user.setId(1);
        this.user.setName("Mattia");
        this.user.setSurname("Olimpico");
        this.user.setAge(22);
        this.user.setBirthday("26/10/1998");
        this.user.setEmail("simone@studenti.unisalento.it");
        this.user.setPassword("password");
        this.user.setMessageList(null);
        this.user.setPostList(null);
        this.user.setNotificationList(null);
        this.user.setProfileImage(this.profileImage);

        this.frontedUser = new User();
        this.frontedUser.setName("Mattia");
        this.frontedUser.setSurname("Olimpico");
        this.frontedUser.setAge(22);
        this.frontedUser.setBirthday("26/10/1998");
        this.frontedUser.setEmail("simone@studenti.unisalento.it");
        this.frontedUser.setPassword("password");
        this.frontedUser.setMessageList(null);
        this.frontedUser.setPostList(null);
        this.frontedUser.setNotificationList(null);
        this.frontedUser.setProfileImage(null);

        userServiceMock = mock(IUserService.class);

        try{when(userServiceMock.updateAge(this.user.getId(), 1000)).thenReturn(this.user);} catch (Exception e){}
        when(userServiceMock.addProfileImage(this.profileImage, this.user.getId())).thenReturn(this.user);
    }

    @Test
    void deleteUserTest(){
        try {
            doNothing().when(userServiceMock).deleteUser(isA(Integer.class));
            userServiceMock.deleteUser(user.getId());
            verify(userServiceMock, times(1)).deleteUser(user.getId());
        }
        catch (UserNotFoundException e){}
    }

    @Test
    void findByIdTest(){
        try{
            assertThat(userService.findById(user.getId()).getId()).isEqualTo(user.getId());
        }
        catch (UserNotFoundException e){}
    }

    @Test
    void findByIdThrowsExTest(){
        Exception ex = assertThrows(UserNotFoundException.class, () -> {
            userService.findById(0);
        });
        assertThat(ex).isNotNull();
    }

    @Test
    void getAllTest(){
        assertThat(userService.getAll()).isNotNull();
    }

    @Test
    void findByEmailTest(){
        try{
            assertThat(userService.findByEmail(user.getEmail())).isNotNull();
            assertThat(userService.findByEmail(user.getEmail()).getEmail()).isEqualTo(user.getEmail());
        }
        catch (UserNotFoundException e){}
    }

    @Test
    void findByEmailThrowExTest(){
        Exception ex = assertThrows(UserNotFoundException.class, () -> {
            userService.findByEmail("notvalidEmail");
        });
        assertThat(ex).isNotNull();
    }

    @Test
    void deleteProfileImageTest(){
        try {
            doNothing().when(userServiceMock).deleteProfileImage(isA(Integer.class));
            userServiceMock.deleteProfileImage(this.profileImage.getId());
            verify(userServiceMock, times(1)).deleteProfileImage(this.profileImage.getId());
        } catch (Exception e){}
    }

    @Test
    void deleteProfileImageThrowExTest(){
        Exception ex = assertThrows(ImageNotFoundException.class, () -> {
            userService.deleteProfileImage(0);
        });
        assertThat(ex).isNotNull();
    }

    @Test
    void addProfileImageTest(){
        assertThat(userServiceMock.addProfileImage(this.profileImage, this.user.getId())).isNotNull();
        assertThat(userServiceMock.addProfileImage(this.profileImage, this.user.getId()).getId()).isEqualTo(this.user.getId());
        assertThat(userServiceMock.addProfileImage(this.profileImage, this.user.getId()).getProfileImage()).isEqualTo(this.profileImage);
    }

    @Test
    void updateAge(){
        try {
            assertThat(userServiceMock.updateAge(this.user.getId(), 1000)).isNotNull();
        } catch (Exception e){}
    }

    @Test
    void updateAgeThrowExTest(){
        Exception ex = assertThrows(UserNotFoundException.class, () -> {
            userService.updateAge(0, 100);
        });
        assertThat(ex).isNotNull();
    }
}
