package it.unisalento.mylinkedin.domain;

import javax.persistence.*;

@Entity
public class RequiredFieldPostType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    int id;

    @ManyToOne(optional = false)
    private PostType postType;

    @ManyToOne(optional = false)
    private RequiredField requiredField;

    public RequiredFieldPostType(){}

    public RequiredFieldPostType(int id, PostType postType, RequiredField requiredField) {
        this.id = id;
        this.postType = postType;
        this.requiredField = requiredField;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public RequiredField getRequiredField() {
        return requiredField;
    }

    public void setRequiredField(RequiredField requiredField) {
        this.requiredField = requiredField;
    }
}
