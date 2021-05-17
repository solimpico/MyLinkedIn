package it.unisalento.mylinkedin.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class PostType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @Column(nullable = false, unique = true, length = 255)
    private String type;

    @OneToMany(mappedBy = "postType", targetEntity = Post.class)
    private List<Post> postList;

    @OneToMany(mappedBy = "postType", targetEntity = RequiredFieldPostType.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL) //Se cancello una tipologia cancello anche i campi obbligatori ad essa associati
    private List<RequiredFieldPostType> requiredFieldPostTypesList;

    public PostType(){}

    public PostType(int id, String type, List<Post> postList, List<RequiredFieldPostType> requiredFieldPostTypesList) {
        this.id = id;
        this.type = type;
        this.postList = postList;
        this.requiredFieldPostTypesList = requiredFieldPostTypesList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public List<RequiredFieldPostType> getRequiredFieldPostTypesList() {
        return requiredFieldPostTypesList;
    }

    public void setRequiredFieldPostTypesList(List<RequiredFieldPostType> requiredFieldPostTypesList) {
        this.requiredFieldPostTypesList = requiredFieldPostTypesList;
    }
}
