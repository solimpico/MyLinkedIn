package it.unisalento.mylinkedin.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String birthday;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", targetEntity = ProfileImage.class, cascade = CascadeType.ALL)
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "user", targetEntity = Notification.class, cascade = CascadeType.ALL)
    private List<Notification>  notificationList;

    @OneToMany(mappedBy = "user", targetEntity = Message.class, cascade = CascadeType.ALL)
    private List<Message> messageList;

    @OneToMany(mappedBy = "user", targetEntity = Post.class, cascade = CascadeType.ALL)
    private List<Post> postList;

    @OneToMany(mappedBy = "user", targetEntity = Comment.class, cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "user", targetEntity = Sns.class, cascade = CascadeType.ALL)
    private List<Sns> snsList;

    public User(){}

    public User(int id, String name, String surname, String birthday, int age, String email, String password, ProfileImage profileImage, List<Notification> notificationList, List<Message> messageList, List<Post> postList, List<Comment> commentList, List<Sns> snsList) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.age = age;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.notificationList = notificationList;
        this.messageList = messageList;
        this.postList = postList;
        this.commentList = commentList;
        this.snsList = snsList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge(){return age;}

    public void setAge(int age){this.age = age;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public List<Sns> getSnsList() {
        return snsList;
    }

    public void setSnsList(List<Sns> snsList) {
        this.snsList = snsList;
    }
}

/*
* nell'annotation @OneToOne -> cascade = CascadeType.ALL sta a specificare l'eliminazione della
* ProfileImage corrispondente all'eleminazione dello User associato
* */