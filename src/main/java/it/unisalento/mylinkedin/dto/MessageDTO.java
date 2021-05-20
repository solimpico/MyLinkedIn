package it.unisalento.mylinkedin.dto;

import it.unisalento.mylinkedin.domain.Message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDTO{

    private int id;
    @NotBlank
    private String message;
    private Date datetime;
    @NotNull
    private int idReceiver;
    @NotNull
    private int idSender;

    private int conversationId;

    private List<MessageDTO> messageDTOList;

    public MessageDTO(){}

    public MessageDTO(int id, String message, Date datetime, int idReceiver, int idSender, int conversationId, List<MessageDTO> messageDTOList) {
        this.id = id;
        this.message = message;
        this.datetime = datetime;
        this.idReceiver = idReceiver;
        this.idSender = idSender;
        this.conversationId = conversationId;
        this.messageDTOList = messageDTOList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(int idReceiver) {
        this.idReceiver = idReceiver;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public List<MessageDTO> getMessageDTOList() {
        return messageDTOList;
    }

    public void setMessageDTOList(List<MessageDTO> messageDTOList) {
        this.messageDTOList = messageDTOList;
    }

    public MessageDTO dtoFromDomain(Message message){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setMessage(message.getMessage());
        messageDTO.setDatetime(message.getDatetime());
        messageDTO.setIdReceiver(message.getIdReceiver());
        messageDTO.setIdSender(message.getUser().getId());
        if(message.getConversation() == null){
            //Nuova conversazione
            messageDTO.setConversationId(0);
        }
        else {
            //conversazione già esistente
            messageDTO.setConversationId(message.getConversation().getId());
        }
        List<MessageDTO> messageOfConversation = new ArrayList<>();
        if(message.getMessageList() != null){
            for (Message mess : message.getMessageList()){
                MessageDTO messDTO = new MessageDTO();
                messageOfConversation.add(messDTO.dtoFromDomain(mess));
            }
        }
        messageDTO.setMessageDTOList(messageOfConversation);
        return messageDTO;
    }
}
