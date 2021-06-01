package it.unisalento.mylinkedin.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@DiscriminatorValue("applicant")
public class Applicant extends User{

    @Column(nullable = false)
    private boolean registered;
    @Column(nullable = false)
    private boolean enabling;

    @OneToMany(mappedBy = "applicant", targetEntity = SkilApplicant.class)
    private List<SkilApplicant> skilApplicantList;

    public Applicant(){}

    public Applicant(int id, String name, String surname, String birthday, int age, String email, String password, ProfileImage profileImage, List<Notification> notificationList, List<Message> messageList, List<Post> postList, List<Comment> comentList, boolean registered, boolean enabling, List<SkilApplicant> skilApplicantList) {
        super(id, name, surname, birthday, age, email, password, profileImage, notificationList, messageList, postList, comentList);
        this.registered = registered;
        this.enabling = enabling;
        this.skilApplicantList = skilApplicantList;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean isEnabling() {
        return enabling;
    }

    public void setEnabling(boolean enabling) {
        this.enabling = enabling;
    }

    public List<SkilApplicant> getSkilApplicantList() {
        return skilApplicantList;
    }

    public void setSkilApplicantList(List<SkilApplicant> skilApplicantList) {
        this.skilApplicantList = skilApplicantList;
    }
}
