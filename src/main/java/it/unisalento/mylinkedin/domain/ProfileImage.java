package it.unisalento.mylinkedin.domain;

import javax.persistence.*;

@Entity
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;
    @Column(length = 600)
    private String Description;

    @Lob
    @Basic(fetch = FetchType.LAZY) //con FetchType.LAZY indichiamo che vogliamo caricare subito l'immagine e non all'invocazione
    @Column(nullable = false, unique = true, length = 255)
    private String profilePicturePath;

    @OneToOne(optional = false)
    private User user;

    public ProfileImage(){}

    public ProfileImage(int id, String description, String profilePicturePath, User user) {
        this.id = id;
        Description = description;
        this.profilePicturePath = profilePicturePath;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

/*
* Nell'annotation @OneToOne, optional = false impone il vincolo tale per cui
* non può esistere una classe  senza la rispettiva classe collegata (in questo caso
* non può esistere una foto profilo (owning side) senza uno user (inverse side))
* */