package it.unisalento.mylinkedin.restcontrollertest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.*;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.restcontrollers.PostRestController;
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
@WebMvcTest(controllers = PostRestController.class)
public class PostRestControllerTest {

    @Value("${security.secret}")
    private String secret;

    @Value("${security.prefix}")
    private String prefix;

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
    private Applicant applicant;
    private Offeror offeror;
    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void initTestEnv(){
        this.jwt = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXR0aWFAc3R1ZGVudGkudW5pc2FsZW50by5pdCIsInJ1b2xvIjoiaXQudW5pc2FsZW50by5teWxpbmtlZGluLmRvbWFpbi5PZmZlcm9yIiwiaXNzIjoiaXNzdWVyIiwiZXhwIjoxNjI0ODk1OTc3LCJpYXQiOjE2MjIyMTc1Nzd9.7j2RY8Gc-d-lRYCGFTq9yPS-L39l31kKproljaXdaco";

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
        this.applicant = new Applicant(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null, null, true, true, null);
        this.offeror = new Offeror(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, null, null, true, true, null);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(this.comment);
        this.user = new User(1, "Prova", "Prova", "10/10/10", 20, "prova@prova.it", "prova", null, null, null, null, commentList, null);
        this.post = new Post(1, true, new Date(), this.user, null, null, new PostType(1, "Prova", new ArrayList<Post>(), null),null, null );
        this.comment = new Comment(1, this.user, "test", new Date(), null, null, this.post);

        when(jwtProvider.decodeJwt(this.jwt)).thenReturn(JWT.require(Algorithm.HMAC256(secret)).build().verify(jwt.replace(prefix, "").trim()));
        try{when(applicantService.findByUserId(this.applicant.getId())).thenReturn(this.applicant);}catch (Exception e ){}
        try{when(offerorService.findByUserId(this.offeror.getId())).thenReturn(this.offeror);} catch (Exception e){}
        try{when(userService.findById(this.user.getId())).thenReturn(this.user);}catch (Exception e){}
        try{when(postService.addPost(this.post, null, null)).thenReturn(this.post);} catch (Exception e){}
        try{when(postService.findById(this.post.getId())).thenReturn(this.post);} catch (Exception e){}
        try{when(commentService.save(this.comment)).thenReturn(this.comment);} catch (Exception e){}
        try{when(commentService.findById(this.comment.getId())).thenReturn(this.comment);}catch (Exception e){}
        when(postService.findVisibleByDate()).thenReturn(new ArrayList<Post>());
        try{when(userService.findById(this.user.getId())).thenReturn(this.user);} catch (Exception e){}


    }

    @Test
    void addPostTest(){
        try{
            mockMvc.perform(post("/post/addPost").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(postDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {}
    }

    @Test
    void deletePostTest(){
        try{
            mockMvc.perform(delete("/post/delete/"+this.post.getId()).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) { }
    }

    @Test
    void addCommentTest(){
        try{
            mockMvc.perform(post("/post/addComment").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE).content(objMapper.writeValueAsString(commentDTO))).andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {}
    }

    @Test
    void deleteCommentTest(){
        try{
            mockMvc.perform(delete("/post/removeComment/"+this.comment.getId()).header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }

    @Test
    void showVisibleByDateTest(){
        try{
            mockMvc.perform(get("/post/showVisibleByDate").header("Authorization", jwt).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        } catch (Exception e) {}
    }


}
