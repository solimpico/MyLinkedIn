package it.unisalento.mylinkedin.restcontrollertest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.domain.Company;
import it.unisalento.mylinkedin.domain.Offeror;
import it.unisalento.mylinkedin.dto.CompanyDTO;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.OfferorRestController;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OfferorRestController.class)
public class OfferorRestControllerTest {

    @Value("${security.secret}")
    private String secret;

    @Value("${security.prefix}")
    private String prefix;

    @MockBean
    IOfferorService offerorService;
    @MockBean
    IUserService userService;
    @MockBean
    JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    private CompanyDTO companyDTO;
    private String jwt;
    private Company company;
    private Offeror offeror;

    @BeforeEach
    void initTestEnv(){
        this.jwt = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXR0aWFAc3R1ZGVudGkudW5pc2FsZW50by5pdCIsInJ1b2xvIjoiaXQudW5pc2FsZW50by5teWxpbmtlZGluLmRvbWFpbi5PZmZlcm9yIiwiaXNzIjoiaXNzdWVyIiwiZXhwIjoxNjI0ODk1OTc3LCJpYXQiOjE2MjIyMTc1Nzd9.7j2RY8Gc-d-lRYCGFTq9yPS-L39l31kKproljaXdaco";

        this.companyDTO = new CompanyDTO();
        this.companyDTO.setName("test");
        this.companyDTO.setAddress("test");
        int[] idOfferors = new int[1];
        idOfferors[0] = 1;
        this.companyDTO.setIdOfferor(idOfferors);
        this.companyDTO.setDescription("test");
        this.company = new Company(0, "test", "test", "test", null);
        this.offeror = new Offeror(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null, true, true, this.company);


        when(jwtProvider.decodeJwt(this.jwt)).thenReturn(JWT.require(Algorithm.HMAC256(secret)).build().verify(jwt.replace(prefix, "").trim()));
        when(offerorService.findCompanyByName(this.company.getName())).thenReturn(this.company);
        try{when(offerorService.findByUserId(this.offeror.getId())).thenReturn(this.offeror);} catch (Exception e){}


    }

    @Test
    void addCompanyTest(){
        try{
            mockMvc.perform(post("/offeror/addCompany").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(companyDTO))).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void deleteCompanyTest(){
        try{
            mockMvc.perform(delete("/offeror/deleteCompany/"+this.company.getId()).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }
}
