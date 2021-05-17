package it.unisalento.mylinkedin.restcontrollertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.dto.PostTypeDTO;
import it.unisalento.mylinkedin.dto.SkilDTO;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.AdminRestController;
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
@WebMvcTest(controllers = AdminRestController.class)
public class AdminRestControllerTest {

    @MockBean
    IApplicantService applicantServiceMock;
    @MockBean
    IOfferorService offerorServiceMock;
    @MockBean
    IUserService userServiceMock;
    @MockBean
    IPostTypeService postTypeServiceMock;
    @MockBean
    IRequiredFieldsService requiredFieldsServiceMock;
    @MockBean
    IPostService postServiceMock;
    @MockBean
    ISkilService skilServiceMock;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    private PostTypeDTO postTypeDTO;
    private SkilDTO skilDTO;

    @BeforeEach
    void initTestEnv(){
        this.postTypeDTO = new PostTypeDTO();
        this.postTypeDTO.setType("Tipo di prova");
        List<String> requiredField = new ArrayList<>();
        requiredField.add("Primo");
        requiredField.add("Secondo");
        this. postTypeDTO.setRequiredFields(requiredField);

        this.skilDTO = new SkilDTO();
        this.skilDTO.setSkilName("Skil di test");
    }



    @Test
    void enablingUserTest(){
        try{
            mockMvc.perform(put("/enablingUser/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void disablingUserTest(){
        try{
            mockMvc.perform(put("/disablingUser/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void confirmRegistrationTest(){
        try{
            mockMvc.perform(put("/confirmReg/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteUserTest(){
        try{
            mockMvc.perform(delete("/delete/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getByIdTest(){
        try{
            mockMvc.perform(get("/getById/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addTypeTest(){
        try{
            mockMvc.perform(post("/admin/addType").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(postTypeDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void showRequiredFieldTest(){
        try{
            mockMvc.perform(get("/admin/showExistingRequiredField").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void showExistingTypeTest(){
        try{
            mockMvc.perform(get("/admin/showExistingType").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addSkilTest(){
        try{
            mockMvc.perform(post("/admin/addSkil").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(skilDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteSkilTest(){
        try{
            mockMvc.perform(delete("/admin/deleteSkil/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void showAllSkilTest(){
        try{
            mockMvc.perform(get("/admin/showAllSkils").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deletePostTypeTest(){
        try{
            mockMvc.perform(delete("/admin/deletePostType/{idPostType}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void hidenPostTest(){
        try{
            mockMvc.perform(put("/admin/hidenPost/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void setPostVisibileTest(){
        try{
            mockMvc.perform(put("/admin/setPostVisible/{id}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void showAllPostTest(){
        try{
            mockMvc.perform(get("/admin/showAllPost").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
