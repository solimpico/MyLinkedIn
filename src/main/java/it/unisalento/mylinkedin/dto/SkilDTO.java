package it.unisalento.mylinkedin.dto;

import it.unisalento.mylinkedin.domain.Skil;

import javax.validation.constraints.NotBlank;

public class SkilDTO{
    private int id;
    @NotBlank
    private String skilName;

    public SkilDTO (){};

    public SkilDTO(int id, String skilName) {
        this.id = id;
        this.skilName = skilName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSkilName() {
        return skilName;
    }

    public void setSkilName(String skilName) {
        this.skilName = skilName;
    }

    public SkilDTO dtoFromDomain(Skil skil){
        return new SkilDTO(skil.getId(), skil.getSkilName());
    }

}
