package it.unisalento.mylinkedin.domain;

import javax.persistence.*;

@Entity
public class SkilPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @ManyToOne(optional = false)
    private Skil skil;
    @ManyToOne(optional = false)
    private Post post;

    public SkilPost(){}

    public SkilPost(int id, Skil skil, Post post) {
        this.id = id;
        this.skil = skil;
        this.post = post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Skil getSkil() {
        return skil;
    }

    public void setSkil(Skil skil) {
        this.skil = skil;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
