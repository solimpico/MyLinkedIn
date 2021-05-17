package it.unisalento.mylinkedin.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;
    @Column(nullable = false, length = 1000)
    private String message;
    @Column(nullable = false)
    private Date datetime;
    @Column(nullable = false)
    private int idReceiver;

    @ManyToOne(optional = false) //non può esistere un messaggio senza user
    private User user;

    @ManyToOne
    private Message conversation;

    @OneToMany(mappedBy = "conversation", targetEntity = Message.class, cascade = CascadeType.ALL)
    private List<Message> messageList;

    public Message(){}

    public Message(int id, String message, Date datetime, int idReceiver, User user, Message conversation, List<Message> messageList) {
        this.id = id;
        this.message = message;
        this.datetime = datetime;
        this.idReceiver = idReceiver;
        this.user = user;
        this.conversation = conversation;
        this.messageList = messageList;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getConversation() {
        return conversation;
    }

    public void setConversation(Message conversation) {
        this.conversation = conversation;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}

/*
* Lo user a cui fa riferimento è il sender inteso come proprietario della conversazione (colui che inizia)
* */