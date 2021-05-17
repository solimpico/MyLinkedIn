package it.unisalento.mylinkedin.iservicestest;

import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.Skil;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.*;
import it.unisalento.mylinkedin.iservices.IApplicantService;
import it.unisalento.mylinkedin.iservices.ISkilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ISkilServiceTest {

    @Autowired
    ISkilService skilService;
    @Mock
    ISkilService skilServiceMock;

    private Skil skil;
    private Skil frontendSkil;
    private int userId;

    @BeforeEach
    void initTestEnv(){
        this.userId = 2;

        this.skil = new Skil();
        this.skil.setId(1);
        this.skil.setSkilName("prima");
        this.skil.setSkilPostList(null);
        this.skil.setSkilUserList(null);

        this.frontendSkil = new Skil();
        this.frontendSkil.setSkilName("prova");
        this.frontendSkil.setSkilPostList(null);
        this.frontendSkil.setSkilUserList(null);

       try{
            when(skilServiceMock.saveSkil(frontendSkil)).thenReturn(frontendSkil);
        }
        catch (SkilException e){
            e.printStackTrace();
        }
    }

    @Test
    void findByIdTest(){
        String skilname = null;
        try{
            skilname = skilService.findById(skil.getId()).getSkilName();
        }
        catch (SkilNotFoundException e){}
        assertThat(skilname).isEqualTo(skil.getSkilName());
    }

    @Test
    void findByIdThrowsExTest(){
        Exception ex = assertThrows(SkilNotFoundException.class, ()->{
            skilService.findById(0);
        });
        assertThat(ex).isNotNull();
    }

    @Test
    void findSkilByUserIdTest(){
        assertThat(skilService.findSkilByUserId(userId)).isNotNull();
    }

    @Test
    void saveSkilTest(){
        int id = 100;
        try{
            id = skilServiceMock.saveSkil(frontendSkil).getId();
        }
        catch (SkilException e){}
        assertThat(id).isEqualTo(0);
    }

    @Test
    void saveSkilThrowsExTest(){
        Exception ex = assertThrows(SkilException.class, () -> {
            skilService.saveSkil(null);
        });
        assertThat(ex).isNotNull();
    }

    @Test
    void deleteSkilTest(){
        skilServiceMock = mock(ISkilService.class);
        try {
            doNothing().when(skilServiceMock).deleteSkil(isA(Integer.class));
            skilServiceMock.deleteSkil(skil.getId());
            verify(skilServiceMock, times(1)).deleteSkil(skil.getId());
        }
        catch (SkilNotFoundException e){}
    }

    @Test
    void findAllTest(){
        assertThat(skilService.findAll()).isNotNull();
    }

}
