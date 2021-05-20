package it.unisalento.mylinkedin.dto;

import it.unisalento.mylinkedin.domain.Company;
import it.unisalento.mylinkedin.domain.Offeror;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class CompanyDTO {

    int id;
    @NotNull
    int[] idOfferor;
    @NotBlank
    String name;
    @NotBlank
    String address;
    String description;

    public CompanyDTO(){}

    public CompanyDTO(int id, int[] idOfferor, String name, String address, String description) {
        this.id = id;
        this.idOfferor = idOfferor;
        this.name = name;
        this.address = address;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getIdOfferor() {
        return idOfferor;
    }

    public void setIdOfferor(int[] idOfferor) {
        this.idOfferor = idOfferor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CompanyDTO dtoFromDomain(Company company){
        int[] offerorId = new int[company.getOfferorList().size()];
        int i = 0;
        for(Offeror offeror : company.getOfferorList()){
            offerorId[i] = offeror.getId();
            i++;
        }

        return new CompanyDTO(company.getId(), offerorId, company.getName(), company.getAddress(), company.getDescription());
    }
}
