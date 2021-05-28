package it.unisalento.mylinkedin.restcontrollertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.dto.*;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.PostRestController;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PostRestController.class)
public class PostRestControllerTest {

    @MockBean
    IPostService postService;
    @MockBean
    IUserService userService;
    @MockBean
    IDataService dataService;
    @MockBean
    IPostTypeService postTypeService;
    @MockBean
    ICommentService commentService;
    @MockBean
    IApplicantService applicantService;
    @MockBean
    IOfferorService offerorService;
    @MockBean
    JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    private PostDTO postDTO;
    private DataDTO dataDTO;
    private CommentDTO commentDTO;
    private String jwt;

    @BeforeEach
    void initTestEnv(){
        this.jwt = jwtProvider.createJwt("test@gmail.com", "test");

        this.dataDTO = new DataDTO();
        this.dataDTO.setData("Data_test");
        this.dataDTO.setField("Field_test");

        this.postDTO = new PostDTO();
        this.postDTO.setType("Prova");
        List<DataDTO> dataDTOList = new ArrayList<>();
        dataDTOList.add(dataDTO);
        this.postDTO.setDataDTOList(dataDTOList);
        this.postDTO.setUserId(1);

        this.commentDTO = new CommentDTO();
        this.commentDTO.setComment("test");
        this.commentDTO.setThread(0);
        this.commentDTO.setAuthorId(1);
        this.commentDTO.setCommentsOfThread(null);
    }

    @Test
    void addPostTest(){
        try{
            mockMvc.perform(post("/post/addPost").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(postDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deletePostTest(){
        try{
            mockMvc.perform(delete("/post/delete/{id}").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addCommentTest(){
        try{
            mockMvc.perform(post("/post/addComment").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(commentDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteCommentTest(){
        try{
            mockMvc.perform(delete("/post/removeComment/{id}").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void showVisibleByDateTest(){
        try{
            mockMvc.perform(get("/post/showVisibleByDate").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
