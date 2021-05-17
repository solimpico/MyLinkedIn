package it.unisalento.mylinkedin.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Skil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;
    @Column(nullable = false, unique = true)
    private String skilName;

    @OneToMany(mappedBy = "skil", targetEntity = SkilPost.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SkilPost> skilPostList;

    @OneToMany(mappedBy = "skil", targetEntity = SkilApplicant.class, fetch = FetchType.LAZY)
    private List<SkilApplicant> skilApplicantList;

    public Skil(){}

    public Skil(int id, String skilName, List<SkilPost> skilPostList, List<SkilApplicant> skilApplicantList) {
        this.id = id;
        this.skilName = skilName;
        this.skilPostList = skilPostList;
        this.skilApplicantList = skilApplicantList;
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

    public void setSkilName(String skill) {
        this.skilName = skill;
    }

    public List<SkilPost> getSkilUserList() {
        return skilPostList;
    }

    public void setSkilUserList(List<SkilApplicant> skilApplicantList) {
        this.skilApplicantList = skilApplicantList;
    }

    public List<SkilPost> getSkilPostList() {
        return skilPostList;
    }

    public void setSkilPostList(List<SkilPost> skilPostList) {
        this.skilPostList = skilPostList;
    }



}

/*
* ATTENZIONE
* LA TABELLA SKIL DEVE ESSERE AVVALORATA SECONDO IL FRAMEWORK e-CF
* */