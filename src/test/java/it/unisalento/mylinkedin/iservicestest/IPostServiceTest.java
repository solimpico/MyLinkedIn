package it.unisalento.mylinkedin.iservicestest;

import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.exceptions.AddPostException;
import it.unisalento.mylinkedin.exceptions.CompanyException;
import it.unisalento.mylinkedin.exceptions.PostException;
import it.unisalento.mylinkedin.exceptions.PostNotFoundException;
import it.unisalento.mylinkedin.iservices.IPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IPostServiceTest {

    @Autowired
    IPostService postService;
    @Mock
    IPostService postServiceMock;

    private Post post;
    private Post modifiedPost;
    private Post frontendPost;
    private PostType postType;
    private List<Post> postList;
    private User user;
    private int idPost;
    private int idSkil;


    @BeforeEach
    void initTestEnv(){
        this.idSkil = 1;

        this.idPost = 29;

        this.user = new User();
        this.user.setId(1);

        this.postList = new ArrayList<>();
        this.postList.add(post);

        this.postType = new PostType();
        this.postType.setType("Offerta");
        this.postType.setRequiredFieldPostTypesList(null);
        this.postType.setId(1);
        this.postType.setPostList(postList);

        this.post = new Post();
        this.post.setId(29);
        this.post.setPublicationDate(new Date());
        this.post.setVisible(true);
        this.post.setPostType(this.postType);
        this.post.setUser(user);
        this.post.setDataList(null);
        this.modifiedPost = this.post;
        this.modifiedPost.setVisible(!this.post.isVisible());

        this.frontendPost = new Post();
        this.frontendPost.setPublicationDate(new Date());
        this.frontendPost.setVisible(true);
        this.frontendPost.setPostType(this.postType);
        this.frontendPost.setUser(user);
        this.post.setDataList(null);

        try {
            when(postServiceMock.addPost(frontendPost, null, null)).thenReturn(frontendPost);
            when(postServiceMock.hidenShowPost(this.post.getId())).thenReturn(this.modifiedPost);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void deleteByIdTest(){
        postServiceMock = mock(IPostService.class);
        try {
            doNothing().when(postServiceMock).deleteById(isA(Integer.class));
            postServiceMock.deleteById(post.getId());
            verify(postServiceMock, times(1)).deleteById(post.getId());
        }
        catch (PostException e){}
    }

    @Test
    void deleteByIdThrowsExTest(){}

    @Test
    void findByIdTest(){
        Post post = new Post();
        try{
            post = postService.findById(idPost);
        }
        catch (PostNotFoundException e){
            e.printStackTrace();
        }
        assertThat(post.getId()).isEqualTo(idPost);
    }

    @Test
    void findByIdThrowsExTest(){
       Exception ex = assertThrows(PostNotFoundException.class, () -> {
            postService.findById(0);
        });
           assertThat(ex).isNotNull();
    }

    @Test
    void findAllTest(){
        assertThat(postService.findAll()).isNotNull();
    }

    @Test
    void findVisibile(){
        assertThat(postService.findVisible()).isNotNull();
    }

    @Test
    void addPostTest(){
        int idPost = 100;
        try{
            idPost = postServiceMock.addPost(frontendPost, null, null).getId();
        }
        catch (AddPostException e){
            e.printStackTrace();
        }
        assertThat(idPost).isEqualTo(0);
    }

    @Test
    void addPostThrowsExTest(){
        Exception ex = assertThrows(AddPostException.class, () -> {
           postService.addPost(null, null, null);
        });
        assertThat(ex).isNotNull();
    }

    @Test
    void findVisibileByDateTest(){
        assertThat(postService.findVisibleByDate()).isNotNull();
        assertThat(postService.findVisibleByDate().get(0).isVisible()).isEqualTo(true);
    }

    @Test
    void findVisibileBySkilTest(){
        assertThat(postService.findVisibleBySkil(idSkil)).isNotNull();
    }

    @Test
    void findVisibleByOfferorTest(){
        assertThat(postService.findVisibleByOfferor()).isNotNull();
        assertThat(postService.findVisibleByOfferor().get(0).isVisible()).isEqualTo(true);
    }

    @Test
    void hidenShowPostTest() throws PostNotFoundException{
        assertThat(postServiceMock.hidenShowPost(this.post.getId())).isNotNull();
        assertThat(postServiceMock.hidenShowPost(this.post.getId()).isVisible()).isEqualTo(this.modifiedPost.isVisible());
    }

    @Test
    void hidenShowPostThrowExTest(){
        Exception ex = assertThrows(PostNotFoundException.class, () -> {
            postService.hidenShowPost(0);
        });
        assertThat(ex).isNotNull();
    }


}
