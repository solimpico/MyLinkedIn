package it.unisalento.mylinkedin.dto;

import javax.validation.constraints.NotBlank;


public class RequiredFieldDTO{
    int id;
    @NotBlank
    String field;

    public RequiredFieldDTO(){}

    public RequiredFieldDTO(int id, String field) {
        this.id = id;
        this.field = field;
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
}
