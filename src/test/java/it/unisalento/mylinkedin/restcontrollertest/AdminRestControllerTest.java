package it.unisalento.mylinkedin.restcontrollertest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.PostTypeDTO;
import it.unisalento.mylinkedin.dto.SkilDTO;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.AdminRestController;
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
@WebMvcTest(controllers = AdminRestController.class)
public class AdminRestControllerTest {

    @Value("${security.secret}")
    private String secret;

    @Value("${security.prefix}")
    private String prefix;

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
    @MockBean
    JwtProvider jwtProvider;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objMapper;

    private User user;
    private Applicant applicant;
    private Offeror offeror;
    private List<Offeror> offerorList;
    private List<Applicant> applicantList;
    private PostType postType;
    private Skil skil;
    private Post post;
    private PostTypeDTO postTypeDTO;
    private SkilDTO skilDTO;
    private String jwt;


    @BeforeEach
    void initTestEnv(){
        this.jwt = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJydW9sbyI6Iml0LnVuaXNhbGVudG8ubXlsaW5rZWRpbi5kb21haW4uQWRtaW5pc3RyYXRvciIsImlzcyI6Imlzc3VlciIsImV4cCI6MTYyNDg5MTUxOSwiaWF0IjoxNjIyMjEzMTE4fQ._mUPCHhYla4dQZDyzWuFiXWuGNux3_RuxZzAOvfowNA";

        this.user = new User(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null);
        this.applicant = new Applicant(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null, true, true, null);
        this.offeror = new Offeror(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null, true, true, null);

        this.offerorList = new ArrayList<>();
        this.offerorList.add(this.offeror);
        this.applicantList = new ArrayList<>();
        this.applicantList.add(this.applicant);

        this.skil = new Skil(0, "test", null ,null);

        this.post = new Post(1, true, new Date(), this.user, null, null, new PostType(1, "Prova", new ArrayList<Post>(), null),null, null );
        this.postType = new PostType(0, "prova", null, null);

        this.postTypeDTO = new PostTypeDTO();
        this.postTypeDTO.setType("Tipo di prova");
        List<String> requiredField = new ArrayList<>();
        requiredField.add("Primo");
        requiredField.add("Secondo");
        this. postTypeDTO.setRequiredFields(requiredField);

        this.skilDTO = new SkilDTO();
        this.skilDTO.setSkilName("Skil di test");

        when(jwtProvider.decodeJwt(this.jwt)).thenReturn(JWT.require(Algorithm.HMAC256(secret)).build().verify(jwt.replace(prefix, "").trim()));
        try{when(applicantServiceMock.confirmAndEnable(this.user.getId())).thenReturn(this.applicant);} catch (Exception e){}
        try{when(offerorServiceMock.confirmAndEnable(this.user.getId())).thenReturn(this.offeror);}catch (Exception e){}
        when(offerorServiceMock.findOfferorRegistrationRequest()).thenReturn(this.offerorList);
        when(applicantServiceMock.findApplicantRegistrationRequest()).thenReturn(this.applicantList);
        try{when(postTypeServiceMock.addPostType(this.postType, null)).thenReturn(this.postType);}catch (Exception e){}
        when(requiredFieldsServiceMock.getAll()).thenReturn(new ArrayList<RequiredField>());
        when(postTypeServiceMock.showAllPostType()).thenReturn(new ArrayList<PostType>());
        try{when(skilServiceMock.saveSkil(this.skil)).thenReturn(this.skil);} catch (Exception e){}
        try{when(skilServiceMock.findById(this.skil.getId())).thenReturn(this.skil);}catch (Exception e){}
        when(skilServiceMock.findAll()).thenReturn(new ArrayList<Skil>());
        try{when(postServiceMock.hidenShowPost(this.post.getId())).thenReturn(this.post);} catch (Exception e){}
        try{when(postServiceMock.findById(this.post.getId())).thenReturn(this.post);} catch (Exception e ){}
        when(postServiceMock.findAll()).thenReturn(new ArrayList<Post>());
        when(applicantServiceMock.save(this.applicant)).thenReturn(this.applicant);
        when(offerorServiceMock.save(this.offeror)).thenReturn(this.offeror);
    }

    @Test
    void getSkilByIdTest(){
        try{
            mockMvc.perform(get("admin/getSkilById/"+this.skil.getId())).andExpect(status().isOk());
        } catch (Exception e){}
    }

    @Test
    void confirmRegistrationTest(){
        try{
            mockMvc.perform(put("admin/confirmReg/"+this.applicant.getId()).contentType(MediaType.APPLICATION_JSON_VALUE).header("Authorization", jwt)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void getRetistrationRequestTest(){
        try{
            mockMvc.perform(get("/admin/getRegistrationRequest").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        }
        catch (Exception e){}
    }

    @Test
    void addTypeTest(){
        try{
            mockMvc.perform(post("/admin/addType").contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(postTypeDTO)).header("Authorization", jwt)).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {}
    }

    @Test
    void showRequiredFieldTest(){
        try{
            mockMvc.perform(get("/admin/showExistingRequiredField").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void addSkilTest(){
        try{
            mockMvc.perform(post("/admin/addSkil").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(skilDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {}
    }

    @Test
    void deleteSkilTest(){
        try{
            mockMvc.perform(delete("/admin/deleteSkil/"+String.valueOf(this.skil.getId())).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void deletePostTypeTest(){
        try{
            mockMvc.perform(delete("/admin/deletePostType/"+String.valueOf(this.postType.getId())).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void hidenShowPostTest(){
        try{
            mockMvc.perform(put("/admin/hidenShowPost/"+String.valueOf(this.post.getId())).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void showAllPostTest(){
        try{
            mockMvc.perform(get("/admin/showAllPost").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void enableDisableUserTest(){
        try{
            mockMvc.perform(put("admin/enableDisableUser/"+this.user.getId()).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void deleteRegistrationRequestTest(){
        try{
            mockMvc.perform(delete("admin/deleteRegistrationRequest/"+this.user.getId()).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        }catch (Exception e ){}
    }

}
