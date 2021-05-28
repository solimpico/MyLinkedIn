package it.unisalento.mylinkedin.restcontrollertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.dto.*;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.UserRestController;
import it.unisalento.mylinkedin.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserRestController.class)
public class UserRestControllerTest {

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
    IPostService postService;
    @MockBean
    JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    private UserDTO userDTO;
    private MessageDTO messageDTO;
    private LoginInputDTO loginInputDTO;
    private ProfileImageDTO profileImageDTO;
    private String jwt;

    @BeforeEach
    void initTestEnv(){
        this.jwt = jwtProvider.createJwt("test@gmail.com", "test");

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
    }

    @Test
    void loginTest(){
        try{
            mockMvc.perform(post("/public/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(loginInputDTO))).andExpect(status().isOk());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void registrationRequestTest(){
        try{
            mockMvc.perform(post("/public/registrationRequest").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(userDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void showVisibleTest(){
        try{
            mockMvc.perform(get("/public/showVisible").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void showPostTest(){
        try{
            mockMvc.perform(get("/public/getPostById/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addProfileImageTest(){
        try{
            mockMvc.perform(post("/addProfileImage").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(profileImageDTO))).andExpect(status().isOk());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void deleteProfileImageTest(){
        try{
            mockMvc.perform(delete("/deleteProfileImage").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e ){e.printStackTrace();}
    }

    @Test
    void sendMessageTest(){
        try{
            mockMvc.perform(post("/user/sendMessage").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(messageDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void showConversationTest(){
        try{
            mockMvc.perform(get("/user/showConversation").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteUserTest(){
        try{
            mockMvc.perform(delete("/user/deleteUser/{id}").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getByIdTest(){
        try{
            mockMvc.perform(get("user/getUserById/{id}").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
