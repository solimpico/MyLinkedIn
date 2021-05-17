package it.unisalento.mylinkedin.domain;

import javax.persistence.*;

@Entity
public class SkilApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @ManyToOne(optional = false)
    private Skil skil;

    @ManyToOne(optional = false)
    private Applicant applicant;

    public SkilApplicant(){}

    public SkilApplicant(int id, Skil skil, Applicant applicant) {
        this.id = id;
        this.skil = skil;
        this.applicant = applicant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Skil getSkil() {
        return skil;
    }

    public void setSkil(Skil skil) {
        this.skil = skil;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}
