package it.unisalento.mylinkedin.restcontrollertest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.*;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.UserRestController;
import it.unisalento.mylinkedin.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserRestController.class)
public class UserRestControllerTest {

    @Value("${security.secret}")
    private String secret;

    @Value("${security.prefix}")
    private String prefix;

    @MockBean
    IUserService userService;
    @MockBean
    IApplicantService applicantService;
    @MockBean
    IOfferorService offerorService;
    @MockBean
    IAdminService adminService;
    @MockBean
    IMessageService messageService;
    @MockBean
    ISkilService skilService;
    @MockBean
    IPostTypeService postTypeServiceMock;
    @MockBean
    IPostService postService;
    @MockBean
    JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    private User user;
    private Offeror offeror;
    private Applicant applicant;
    private Administrator administrator;
    private List<Post> postList;
    private Message message;
    private List<Message> messageList;
    private Post post;
    private UserDTO userDTO;
    private MessageDTO messageDTO;
    private LoginInputDTO loginInputDTO;
    private ProfileImageDTO profileImageDTO;
    private String jwt;

    @BeforeEach
    void initTestEnv(){
        this.jwt = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXR0aWFAc3R1ZGVudGkudW5pc2FsZW50by5pdCIsInJ1b2xvIjoiaXQudW5pc2FsZW50by5teWxpbmtlZGluLmRvbWFpbi5PZmZlcm9yIiwiaXNzIjoiaXNzdWVyIiwiZXhwIjoxNjI0NzE1ODAxLCJpYXQiOjE2MjIwMzc0MDF9.l3Aw2kEtYyNnz_Io5QCqBZw_D3m7sSRDWgk_Fk3yGi8";

        this.loginInputDTO = new LoginInputDTO();
        this.loginInputDTO.setEmail("test@gmail.com");
        this.loginInputDTO.setPassword("test");

        this.userDTO = new UserDTO();
        this.userDTO.setName("Prova");
        this.userDTO.setSurname("Prova");
        this.userDTO.setBirthday("10/10/10");
        this.userDTO.setAge(20);
        this.userDTO.setRole("Applicant");
        this.userDTO.setEmail("prova@prova.it");
        this.userDTO.setEmailToVerify("prova@prova.it");
        this.userDTO.setPassword("prova");
        this.userDTO.setPasswordToVerify("prova");

        this.messageDTO = new MessageDTO();
        this.messageDTO.setMessage("prova");
        this.messageDTO.setMessageDTOList(null);
        this.messageDTO.setConversationId(0);
        this.messageDTO.setIdSender(1);
        this.messageDTO.setIdReceiver(2);

        this.profileImageDTO = new ProfileImageDTO();
        this.profileImageDTO.setPath("Un/path/di/test");

        this.user = new User(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null);
        this.applicant = new Applicant(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null, true, true, null);
        this.offeror = new Offeror(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null, true, true, null);
        this.administrator = new Administrator(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null);

        List<Data> dataList = new ArrayList<>();
        dataList.add(new Data(1, "test", "test", null, this.post));
        this.post = new Post(1, true, new Date(), this.user, null, dataList, new PostType(1, "Prova", this.postList, null),null, null );
        this.postList = new ArrayList<>();
        this.postList.add(this.post);
        this.message = new Message(1, "prova", new Date(), 2, this.user, null, null);
        this.messageList = new ArrayList<>();
        this.messageList.add(this.message);

        when(jwtProvider.createJwt("test@gmail.com", "test")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXR0aWFAc3R1ZGVudGkudW5pc2FsZW50by5pdCIsInJ1b2xvIjoiaXQudW5pc2FsZW50by5teWxpbmtlZGluLmRvbWFpbi5PZmZlcm9yIiwiaXNzIjoiaXNzdWVyIiwiZXhwIjoxNjI0NzE1ODAxLCJpYXQiOjE2MjIwMzc0MDF9.l3Aw2kEtYyNnz_Io5QCqBZw_D3m7sSRDWgk_Fk3yGi8");
        when(jwtProvider.decodeJwt(this.jwt)).thenReturn(JWT.require(Algorithm.HMAC256(secret)).build().verify(jwt.replace(prefix, "").trim()));
        try{when(userService.findByEmail(this.loginInputDTO.getEmail())).thenReturn(this.user);} catch (Exception e){}
        try{when(offerorService.saveRegistrationtRequestOfferor(this.user)).thenReturn(this.offeror);}catch (Exception e){}
        try{when(applicantService.saveRegistrationtRequestApplicant(this.user)).thenReturn(this.applicant);}catch (Exception e){}
        try{when(adminService.saveRegistrationRequestAdmin(this.user)).thenReturn(this.administrator);}catch (Exception e){}
        when(postService.findVisible()).thenReturn(this.postList);
        try{when(postService.findById(1)).thenReturn(this.postList.get(0));}catch (Exception e){}
        try{when(userService.addProfileImage(null, this.user.getId())).thenReturn(this.user);}catch (Exception e){}
        try{when(userService.deleteProfileImage(this.user.getId())).thenReturn(this.user);}catch (Exception e){}
        try{when(userService.findById(this.user.getId())).thenReturn(this.user);}catch (Exception e){}
        try{when(messageService.findById(this.message.getId())).thenReturn(this.message);}catch (Exception e){}
        try{when(messageService.saveMessage(this.message)).thenReturn(this.message);} catch (Exception e){}
        try{when(messageService.getMessageByUserId(this.user.getId())).thenReturn(this.messageList);} catch (Exception e){}
        try{when(applicantService.findByUserId(this.applicant.getId())).thenReturn(this.applicant);} catch (Exception e){}
        try{when(offerorService.findByUserId(this.offeror.getId())).thenReturn(this.offeror);} catch (Exception e){}
        when(userService.getAll()).thenReturn(new ArrayList<User>());
        when(skilService.findAll()).thenReturn(new ArrayList<Skil>());
        when(postTypeServiceMock.showAllPostType()).thenReturn(new ArrayList<PostType>());
        try{when(userService.updateAge(this.user.getId(), 40)).thenReturn(this.user);} catch (Exception e){}


    }

    @Test
    void loginTest(){
        try{
            mockMvc.perform(post("/public/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(loginInputDTO))).andExpect(status().isOk());
        }
        catch (Exception e){ }
    }

    @Test
    void registrationRequestTest(){
        try{
            mockMvc.perform(post("/public/registrationRequest").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(userDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {}
    }

    @Test
    void showVisibleTest(){
        try{
            mockMvc.perform(get("/public/showVisible").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void showPostTest(){
        try{
            String value = String.valueOf(this.postList.get(0).getId());
            mockMvc.perform(get("/public/getPostById/"+value).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) { }
    }


    @Test
    void addProfileImageTest(){
        try{
            mockMvc.perform(post("/addProfileImage").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(profileImageDTO))).andExpect(status().isOk());
        } catch (Exception e){}
    }

    @Test
    void deleteProfileImageTest(){
        try{
            mockMvc.perform(delete("/deleteProfileImage").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e ){}
    }

    @Test
    void sendMessageTest(){
        try{
            mockMvc.perform(post("/sendMessage").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(messageDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {}
    }

    @Test
    void showConversationTest(){
        try{
            mockMvc.perform(get("/showConversation").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void getProfileImageTest(){
        try{
            mockMvc.perform(get("/getProfileImage").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void deleteUserTest(){
        try{
            String value = String.valueOf(this.user.getId());
            mockMvc.perform(delete("/deleteUser/"+value).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void getByIdTest(){
        try{
            String value = String.valueOf(this.user.getId());
            mockMvc.perform(get("public/getUserById/"+value).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void getAllUsersTest(){
        try{
            mockMvc.perform(get("/getAllUsers").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        }
        catch (Exception e){}
    }

    @Test
    void showAllSkilTest(){
        try{
            mockMvc.perform(get("/showAllSkils").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void showExistingTypeTest(){
        try{
            mockMvc.perform(get("/showExistingType").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void updateAgeTest(){
        try{
            mockMvc.perform(put("/updateAge/"+this.user.getAge()).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e){}
    }
}
