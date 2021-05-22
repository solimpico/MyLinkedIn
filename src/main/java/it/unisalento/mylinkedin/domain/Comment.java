package it.unisalento.mylinkedin.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int id;

    @Column(nullable = false)
    private int authorId;
    @Column(nullable = false, length = 500)
    private String comment;
    @Column(nullable = false)
    private Date datetime;

    //parent
    @ManyToOne
    Comment thread;

    //childs
    @OneToMany(mappedBy = "thread", targetEntity = Comment.class, cascade = CascadeType.ALL)
    List<Comment> commentList;

    @ManyToOne(optional = false)
    Post post;

    public Comment(){}

    public Comment(int id, int authorId, String comment, Date datetime, Comment thread, List<Comment> commentList, Post post) {
        this.id = id;
        this.authorId = authorId;
        this.comment = comment;
        this.datetime = datetime;
        this.thread = thread;
        this.commentList = commentList;
        this.post = post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Comment getThread() {
        return thread;
    }

    public void setThread(Comment thread) {
        this.thread = thread;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
