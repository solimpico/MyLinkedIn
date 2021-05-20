package it.unisalento.mylinkedin.restcontrollertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.dto.MessageDTO;
import it.unisalento.mylinkedin.dto.PostTypeDTO;
import it.unisalento.mylinkedin.dto.SkilDTO;
import it.unisalento.mylinkedin.dto.UserDTO;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.UserRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    private UserDTO userDTO;
    private MessageDTO messageDTO;

    @BeforeEach
    void initTestEnv(){
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
    }

    @Test
    void enablingUserTest(){
        try{
            mockMvc.perform(put("/user/enablingUser/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void disablingUserTest(){
        try{
            mockMvc.perform(put("/user/disablingUser/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void registrationRequestTest(){
        try{
            mockMvc.perform(post("/user/registrationRequest").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(userDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void sendMessageTest(){
        try{
            mockMvc.perform(post("/user/sendMessage").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(messageDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void showConversationTest(){
        try{
            mockMvc.perform(get("/user/showConversation/{userId}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteUserTest(){
        try{
            mockMvc.perform(delete("/user/delete/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getByIdTest(){
        try{
            mockMvc.perform(get("user/getById/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
