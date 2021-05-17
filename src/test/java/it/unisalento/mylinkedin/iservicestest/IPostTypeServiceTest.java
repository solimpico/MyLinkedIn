package it.unisalento.mylinkedin.iservicestest;

import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.PostType;
import it.unisalento.mylinkedin.domain.RequiredField;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.*;
import it.unisalento.mylinkedin.iservices.IApplicantService;
import it.unisalento.mylinkedin.iservices.IPostTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IPostTypeServiceTest {

    @Autowired
    IPostTypeService postTypeService;
    @Mock
    IPostTypeService postTypeServiceMock;

    private PostType postType;
    private PostType frontendPostType;
    private List<String> requiredFieldList;

    @BeforeEach
    void initTestEnv(){

        this.requiredFieldList = new ArrayList<>();
        this.requiredFieldList.add("prova");

        this.postType = new PostType();
        this.postType.setId(1);
        this.postType.setType("Offerta");
        this.postType.setPostList(null);
        this.postType.setRequiredFieldPostTypesList(null);

        this.frontendPostType = new PostType();
        this.postType.setType("Offerta");
        this.frontendPostType.setPostList(null);
        this.frontendPostType.setRequiredFieldPostTypesList(null);

        try {
            when(postTypeServiceMock.addPostType(frontendPostType, requiredFieldList)).thenReturn(frontendPostType);
        }
        catch (AddPostTypeException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addPostTypeTest(){
        int idPT = 100;
        try{
            idPT = postTypeServiceMock.addPostType(frontendPostType, requiredFieldList).getId();
        }
        catch (AddPostTypeException e) {
            e.printStackTrace();
        }
        assertThat(idPT).isEqualTo(0);
    }

    @Test
    void addPostTypeThrowsExTest(){
        Exception ex = assertThrows(AddPostTypeException.class, ()->{
            postTypeService.addPostType(null, null);
        });
        assertThat(ex).isNotNull();
    }

    @Test
    void showAllPostTypeTest(){
        assertThat(postTypeService.showAllPostType()).isNotNull();
    }

    @Test
    void findByNameTest(){
        assertThat(postTypeService.findByName(postType.getType())).isNotNull();
        assertThat(postTypeService.findByName(postType.getType()).getType()).isEqualTo(postType.getType());
        assertThat(postTypeService.findByName(postType.getType()).getId()).isEqualTo(postType.getId());
    }

    @Test
    void deleteByIdTest(){
        postTypeServiceMock = mock(IPostTypeService.class);
        try {
            doNothing().when(postTypeServiceMock).deletePostTypeById(isA(Integer.class));
            postTypeServiceMock.deletePostTypeById(postType.getId());
            verify(postTypeServiceMock, times(1)).deletePostTypeById(postType.getId());
        }
        catch (PostException e){}
    }


}
