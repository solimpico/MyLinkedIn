package it.unisalento.mylinkedin.iservicestest;

import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.Comment;
import it.unisalento.mylinkedin.domain.Post;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.CommentNotFoundException;
import it.unisalento.mylinkedin.exceptions.PostNotFoundException;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IApplicantService;
import it.unisalento.mylinkedin.iservices.ICommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ICommentServiceTest {

    @Autowired
    ICommentService commentService;
    @Mock
    ICommentService commentServiceMock;
    @Mock
    ICommentService commentSerMock;

    private Comment comment;
    private Comment frontendComment;
    private Post post;
    private int idPost;
    private int idComment;

    @BeforeEach
    void initTestEnv(){
        this.idPost = 1;

        this.idComment = 1;

        this.post = new Post();

        this.frontendComment = new Comment();
        this.frontendComment.setComment("Prova");
        this.frontendComment.setCommentList(null);
        this.frontendComment.setThread(null);
        this.frontendComment.setAuthorId(100);
        this.frontendComment.setDatetime(new Date());
        this.frontendComment.setPost(this.post);

        this.comment = new Comment();
        this.comment.setId(3);
        this.comment.setComment("Prima thrad del post 1");
        this.comment.setCommentList(null);
        this.comment.setThread(null);
        this.comment.setAuthorId(1);
        this.comment.setDatetime(new Date());
        this.comment.setPost(this.post);

        try{
            when(commentServiceMock.save(frontendComment)).thenReturn(frontendComment);
        }
        catch (PostNotFoundException e){
            e.printStackTrace();
        }

    }

    @Test
    void saveTest(){
        int id = 1000;
        try{
            id = commentServiceMock.save(frontendComment).getId();
        }
        catch (PostNotFoundException e){
            e.printStackTrace();
        }
        assertThat(id).isEqualTo(0);
    }

    @Test
    void saveExTest(){
        frontendComment.setPost(null);
        Exception ex = assertThrows(PostNotFoundException.class, () -> {
            commentService.save(frontendComment);
        });
        assertThat(ex).isNotNull();
    }

    @Test
    void findByIdTest(){
        try {
            assertThat(commentService.findById(this.comment.getId())).isNotNull();
            assertThat(commentService.findById(this.comment.getId()).getComment()).isEqualTo(this.comment.getComment());
        }
        catch (CommentNotFoundException e){
            e.printStackTrace();
        }
    }

    @Test
    void findByIdExTest(){
        Exception ex = assertThrows(CommentNotFoundException.class, () -> {
            commentService.findById(0);
        });
        assertThat(ex).isNotNull();
    }

    @Test
    void deleteByIdTest(){
        commentServiceMock = mock(ICommentService.class);
        try {
            doNothing().when(commentServiceMock).deleteById(isA(Integer.class));
            commentServiceMock.deleteById(comment.getId());
            verify(commentServiceMock, times(1)).deleteById(comment.getId());
        }
        catch (CommentNotFoundException e){}
    }
}
