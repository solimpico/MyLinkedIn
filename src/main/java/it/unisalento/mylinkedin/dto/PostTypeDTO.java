package it.unisalento.mylinkedin.dto;

import it.unisalento.mylinkedin.domain.PostType;
import it.unisalento.mylinkedin.domain.RequiredFieldPostType;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


public class PostTypeDTO{
    private int id;
    @NotBlank
    private String type;
    private List<String> requiredFields;

    public PostTypeDTO(){}

    public PostTypeDTO(int id, String type, List<String> requiredFields) {
        this.id = id;
        this.type = type;
        this.requiredFields = requiredFields;
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

    public List<String> getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(List<String> requiredFields) {
        this.requiredFields = requiredFields;
    }

    public PostTypeDTO dtoFromDomain(PostType postType){
        List<String> fieldList = new ArrayList<String>();
        for (RequiredFieldPostType requiredFieldPostType : postType.getRequiredFieldPostTypesList()) {
            fieldList.add(requiredFieldPostType.getRequiredField().getRequiredField());
        }
        return new PostTypeDTO(postType.getId(), postType.getType(), fieldList);
    }
}
