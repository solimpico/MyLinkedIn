package it.unisalento.mylinkedin.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    int id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String address;
    String description;

    @OneToMany(mappedBy = "company", targetEntity = Offeror.class)
    List<Offeror> offerorList;

    public Company(){}

    public Company(int id, String name, String address, String description, List<Offeror> offerorList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.offerorList = offerorList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Offeror> getOfferorList() {
        return offerorList;
    }

    public void setOfferorList(List<Offeror> offerorList) {
        this.offerorList = offerorList;
    }
}
