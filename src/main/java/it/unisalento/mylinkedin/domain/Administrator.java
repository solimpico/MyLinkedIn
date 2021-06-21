package it.unisalento.mylinkedin.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue("administrator")
public class Administrator extends User{

    public Administrator(){}

    public Administrator(int id, String name, String surname, String birthday, int age, String email, String password, ProfileImage profileImage, List<Notification> notificationList, List<Message> messageList, List<Post> postList, List<Comment> commentList, List<Sns> snsList) {
        super(id, name, surname, birthday, age, email, password, profileImage, notificationList, messageList, postList, commentList, snsList);
    }
}
