package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.MessageRepository;
import it.unisalento.mylinkedin.domain.Message;
import it.unisalento.mylinkedin.exceptions.MessageException;
import it.unisalento.mylinkedin.iservices.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    @Transactional(rollbackOn = MessageException.class)
    public Message saveMessage(Message message) throws MessageException{
        try {
            return messageRepository.save(message);
        }
        catch (Exception e){
            throw new MessageException();
        }
    }

    @Override
    @Transactional
    public List<Message> getMessageByUserId(int idUser) {
        return messageRepository.findByUserId(idUser);
    }

    @Override
    @Transactional
    public Message findById(int idMessage) throws MessageException{
        return messageRepository.findById(idMessage).orElseThrow( () -> new MessageException());
    }
}
