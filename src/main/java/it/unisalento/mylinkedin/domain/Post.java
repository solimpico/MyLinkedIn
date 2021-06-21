package it.unisalento.mylinkedin.domain;

import it.unisalento.mylinkedin.dto.PostDTO;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IDataService;
import it.unisalento.mylinkedin.iservices.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private Date publicationDate;

    @ManyToOne(optional = false) //diversi post possono essere creati dallo stesso user (il post deve obbligatoriamente essere creato da uno user)
    private User user;

    @OneToMany(mappedBy = "post", targetEntity = Data.class, cascade = CascadeType.ALL)
    private List<Data> dataList;

    @ManyToOne(optional = false)
    private PostType postType;

    @OneToMany(mappedBy = "post", targetEntity = SkilPost.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SkilPost> skilPostList;

    @OneToMany(mappedBy = "post", targetEntity = Comment.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "post", targetEntity = Notification.class, cascade = CascadeType.ALL)
    private List<Notification> notificationList;

    public Post(){}

    public Post(int id, boolean visible, Date publicationDate, User user,List<Notification> notificationList, List<Data> dataList, PostType postType, List<Comment> commentList, List<SkilPost> skilPostList) {
        this.id = id;
        this.visible = visible;
        this.publicationDate = publicationDate;
        this.user = user;
        this.notificationList = notificationList;
        this.dataList = dataList;
        this.postType = postType;
        this.commentList = commentList;
        this.skilPostList = skilPostList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<SkilPost> getSkilPostList() {
        return skilPostList;
    }

    public void setSkilPostList(List<SkilPost> skilPostList) {
        this.skilPostList = skilPostList;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }
}
