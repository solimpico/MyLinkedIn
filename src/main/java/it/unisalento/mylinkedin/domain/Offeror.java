package it.unisalento.mylinkedin.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("offeror")
public class Offeror extends User{

    @Column(nullable = false)
    private boolean registered;
    @Column(nullable = false)
    private boolean enabling;

    @ManyToOne
    Company company;

    public Offeror() {}

    public Offeror(int id, String name, String surname, String birthday, int age, String email, String password, ProfileImage profileImage, List<Notification> notificationList, List<Message> messageList, List<Post> postList,List<Comment> commentList, List<Sns> snsList, boolean registered, boolean enabling, Company company) {
        super(id, name, surname, birthday, age, email, password, profileImage, notificationList, messageList, postList, commentList, snsList);
        this.registered = registered;
        this.enabling = enabling;
        this.company = company;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
