package it.unisalento.mylinkedin.domain;

import javax.persistence.*;

@Entity
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @Column(nullable = false)
    private String field;

    @Column(length = 800)
    private String data;

    @Basic(fetch = FetchType.LAZY) //con FetchType.LAZY indichiamo che vogliamo caricare subito l'immagine e non all'invocazione
    @Column(length = 255)
    private String dataFilePath;

    @ManyToOne(optional = false)
    private Post post;

    public Data(){}

    public Data(int id, String field, String data, String dataFilePath, Post post) {
        this.id = id;
        this.field = field;
        this.data = data;
        this.dataFilePath = dataFilePath;
        this.post = post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataImage() {
        return dataFilePath;
    }

    public void setDataImage(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
