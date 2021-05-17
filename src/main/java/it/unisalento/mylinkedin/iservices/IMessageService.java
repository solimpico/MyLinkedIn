package it.unisalento.mylinkedin.iservices;

import it.unisalento.mylinkedin.domain.Message;
import it.unisalento.mylinkedin.exceptions.MessageException;

import java.util.List;

public interface IMessageService {

    Message saveMessage(Message message) throws MessageException;
    List<Message> getMessageByUserId(int idUser);
    Message findById(int idMessage) throws MessageException;
}
