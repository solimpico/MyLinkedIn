package it.unisalento.mylinkedin.iservicestest;

import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.Message;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.MessageException;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IApplicantService;
import it.unisalento.mylinkedin.iservices.IMessageService;
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
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IMessageServiceTest {

    @Autowired
    IMessageService messageService;
    @Mock
    IMessageService messageServiceMock;

    private Message message;
    private Message frontendMessage;
    private User user;
    private int userId;

    @BeforeEach
    void initTestEnv(){
        this.userId = 1;

        this.user = new User();

        this.frontendMessage = new Message();
        this.frontendMessage.setMessage("messaggio di prova");
        this.frontendMessage.setMessageList(null);
        this.frontendMessage.setConversation(null);
        this.frontendMessage.setDatetime(new Date());
        this.frontendMessage.setIdReceiver(1);
        this.frontendMessage.setUser(user);

        this.message = new Message();
        this.message.setId(1);
        this.message.setMessage("messaggio di prova");
        this.message.setMessageList(null);
        this.message.setConversation(null);
        this.message.setDatetime(new Date());
        this.message.setIdReceiver(1);
        this.message.setUser(user);

        try {
            when(messageServiceMock.saveMessage(frontendMessage)).thenReturn(frontendMessage);
            when(messageServiceMock.findById(message.getId())).thenReturn(message);
        }
        catch (MessageException e){
            e.printStackTrace();
        }

    }

    @Test
    void getMessageByUserIdTest(){
        assertThat(messageService.getMessageByUserId(userId)).isNotNull();
        assertThat(messageService.getMessageByUserId(userId).get(0).getUser().getId()).isEqualTo(userId);
    }

    @Test
    void saveMessageTest(){
        int id = 100;
        try{
            id = messageServiceMock.saveMessage(frontendMessage).getId();

        }
        catch (MessageException e){
            e.printStackTrace();
        }
        assertThat(id).isEqualTo(0);
    }

    @Test
    void saveMessageThrowsExTest(){
        frontendMessage.setUser(null);
        Exception exp = assertThrows(MessageException.class, () ->{
           messageService.saveMessage(frontendMessage);
        });
        assertThat(exp).isNotNull();
    }

    @Test
    void findByIdTest(){
        try {
            assertThat(messageServiceMock.findById(message.getId()).getId()).isEqualTo(message.getId());
            assertThat(messageServiceMock.findById(message.getId()).getMessage()).isEqualTo(message.getMessage());
        }
        catch (MessageException e){
            e.printStackTrace();
        }
    }

    @Test
    void findByIdThrowsExTest(){
        Exception ex = assertThrows(MessageException.class, () -> {
           messageService.findById(0);
        });
        assertThat(ex).isNotNull();
    }


}
