package it.unisalento.mylinkedin.restcontrollertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.dto.ApplicantDTO;
import it.unisalento.mylinkedin.dto.PdfDTO;
import it.unisalento.mylinkedin.dto.PostTypeDTO;
import it.unisalento.mylinkedin.dto.SkilDTO;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.ApplicantRestController;
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
@WebMvcTest(controllers = ApplicantRestController.class)
public class ApplicantRestControllerTest {

    @MockBean
    IPostService postService;
    @MockBean
    IApplicantService applicantService;
    @MockBean
    ISkilService skilService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    private PdfDTO pdfDTO;
    private ApplicantDTO applicantDTO;

    @BeforeEach
    void initTestEnv(){
        this.pdfDTO = new PdfDTO();
        int[] idPostArray = new int[2];
        idPostArray[0] = 1;
        idPostArray[1] = 2;
        this.pdfDTO.setIdPostArray(idPostArray);

        this.applicantDTO = new ApplicantDTO();
        this.applicantDTO.setName("Test");
        this.applicantDTO.setSurname("Test");
        this.applicantDTO.setId(1);
        this.applicantDTO.setEnabling(true);
        this.applicantDTO.setRegistered(true);
        int[] skilArray = new int[2];
        skilArray[0] = 1;
        skilArray[1] = 3;
        this.applicantDTO.setSkilIdArray(skilArray);
        this.applicantDTO.setEmail("test@test.com");

    }

    @Test
    void savePostsOnPdfTest(){
        try{
            mockMvc.perform(post("/applicant/saveOnPdf").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(pdfDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void ShowVisibleByOfferorTest(){
        try{
            mockMvc.perform(get("applicant/showVisibleByOfferor").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void ShowVisibleBySkilTest(){
        try{
            mockMvc.perform(get("applicant/showVisibleBySkil/{idUser}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addSkilToApplicantTest(){
        try{
            mockMvc.perform(post("/applicant/addSkilToApplicant").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(applicantDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
