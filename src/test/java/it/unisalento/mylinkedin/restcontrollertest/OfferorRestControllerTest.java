package it.unisalento.mylinkedin.restcontrollertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.dto.CompanyDTO;
import it.unisalento.mylinkedin.dto.PostTypeDTO;
import it.unisalento.mylinkedin.dto.SkilDTO;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.OfferorRestController;
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
@WebMvcTest(controllers = OfferorRestController.class)
public class OfferorRestControllerTest {

    @MockBean
    IOfferorService offerorService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    private CompanyDTO companyDTO;

    @BeforeEach
    void initTestEnv(){
        this.companyDTO = new CompanyDTO();
        this.companyDTO.setName("test");
        this.companyDTO.setAddress("test");
        int[] idOfferors = new int[1];
        idOfferors[0] = 1;
        this.companyDTO.setIdOfferor(idOfferors);
        this.companyDTO.setDescription("test");
    }

    @Test
    void addCompanyTest(){
        try{
            mockMvc.perform(post("/offeror/addCompany").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(companyDTO))).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteCompanyTest(){
        try{
            mockMvc.perform(delete("/offeror/deleteCompany/{idCompany}").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
