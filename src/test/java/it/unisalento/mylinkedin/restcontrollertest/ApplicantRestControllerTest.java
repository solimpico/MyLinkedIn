package it.unisalento.mylinkedin.restcontrollertest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.ApplicantDTO;
import it.unisalento.mylinkedin.dto.PdfDTO;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.ApplicantRestController;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ApplicantRestController.class)
public class ApplicantRestControllerTest {

    @Value("${security.secret}")
    private String secret;

    @Value("${security.prefix}")
    private String prefix;

    @MockBean
    IPostService postService;
    @MockBean
    IApplicantService applicantService;
    @MockBean
    ISkilService skilService;
    @MockBean
    JwtProvider jwtProvider;
    @MockBean
    IUserService userService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    private PdfDTO pdfDTO;
    private ApplicantDTO applicantDTO;
    private String jwt;
    private Post post;
    private User user;
    private Skil skil;
    private Applicant applicant;

    @BeforeEach
    void initTestEnv(){
        this.jwt = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaW1vbmVAc3R1ZGVudGkudW5pc2FsZW50by5pdCIsInJ1b2xvIjoiaXQudW5pc2FsZW50by5teWxpbmtlZGluLmRvbWFpbi5BcHBsaWNhbnQiLCJpc3MiOiJpc3N1ZXIiLCJleHAiOjE2MjQ4OTUxODIsImlhdCI6MTYyMjIxNjc4Mn0.ygd3BXhW7H37GS92fsn6ctGygH2dKeq01KHMaZB-TWg";

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

        this.user = new User(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null, null);
        this.post = new Post(1, true, new Date(), this.user, null, null, new PostType(1, "Prova", new ArrayList<Post>(), null),null, null );
        this.applicant = new Applicant(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null, null, true, true, null);
        this.skil = new Skil(0, "test", null ,null);

        when(jwtProvider.decodeJwt(this.jwt)).thenReturn(JWT.require(Algorithm.HMAC256(secret)).build().verify(jwt.replace(prefix, "").trim()));
        try{when(postService.findById(this.post.getId())).thenReturn(this.post);} catch (Exception e ){}
        when(postService.findVisibleByOfferor()).thenReturn(new ArrayList<Post>());
        try{when(applicantService.findByUserId(this.applicant.getId())).thenReturn(this.applicant);}catch (Exception e ){}
        when(postService.findVisibleBySkil(this.skil.getId())).thenReturn(new ArrayList<Post>());
        when(skilService.findSkilByUserId(this.user.getId())).thenReturn(new ArrayList<Skil>());
        try{when(skilService.findById(this.skil.getId())).thenReturn(this.skil);}catch (Exception e){}

    }

    @Test
    void savePostsOnPdfTest(){
        try{
            mockMvc.perform(post("/applicant/saveOnPdf").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(pdfDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {}
    }

    @Test
    void ShowVisibleByOfferorTest(){
        try{
            mockMvc.perform(get("applicant/showVisibleByOfferor").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void ShowVisibleBySkilTest(){
        try{
            mockMvc.perform(get("applicant/showVisibleBySkil").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) { }
    }

    @Test
    void addSkilToApplicantTest(){
        try{
            mockMvc.perform(post("/applicant/addSkilToApplicant").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(applicantDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {}
    }

}
