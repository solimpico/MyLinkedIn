package it.unisalento.mylinkedin.domain;

import jdk.dynalink.linker.LinkerServices;
import org.hibernate.validator.constraints.CodePointLength;

import javax.persistence.*;
import java.util.List;

@Entity
public class RequiredField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @Column(nullable = false, unique = true, length = 255)
    private String requiredField;

    @OneToMany(mappedBy = "requiredField", targetEntity = RequiredFieldPostType.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RequiredFieldPostType> requiredFieldPostTypeList;

    public RequiredField(){}

    public RequiredField(int id, String requiredField, List<RequiredFieldPostType> requiredFieldPostTypeList) {
        this.id = id;
        this.requiredField = requiredField;
        this.requiredFieldPostTypeList = requiredFieldPostTypeList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequiredField() {
        return requiredField;
    }

    public void setRequiredField(String requiredField) {
        this.requiredField = requiredField;
    }

    public List<RequiredFieldPostType> getRequiredFieldPostTypeList() {
        return requiredFieldPostTypeList;
    }

    public void setRequiredFieldPostTypeList(List<RequiredFieldPostType> requiredFieldPostTypeList) {
        this.requiredFieldPostTypeList = requiredFieldPostTypeList;
    }
}
