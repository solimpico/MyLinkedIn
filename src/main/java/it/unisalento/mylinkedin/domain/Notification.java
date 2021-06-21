package it.unisalento.mylinkedin.domain;

import javax.persistence.*;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @Column(nullable = false)
    private String message;

    @ManyToOne(optional = false) //non può esistere una notifica senza uno user proprietario
    private User user;

    @ManyToOne(optional = false)
    private Post post;

    public Notification(){}

    public Notification(int id, String message, User user, Post post) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.post = post;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
