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

    @Column(nullable = false)
    private boolean view;

    @ManyToOne(optional = false) //non può esistere una notifica senza uno user proprietario
    private User user;

    @OneToOne
    private Post post;

    public Notification(){}

    public Notification(int id, String message, boolean view, User user, Post post) {
        this.id = id;
        this.message = message;
        this.view = view;
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

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
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
