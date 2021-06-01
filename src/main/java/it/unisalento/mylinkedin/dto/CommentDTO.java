package it.unisalento.mylinkedin.dto;

import it.unisalento.mylinkedin.domain.Comment;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CommentDTO {
    private int id;
    private String author;
    private int authorId;
    @NotBlank
    private String comment;

    private Date datetime;

    //riferimento al comment parent
    @NotNull
    private int thread;

    private List<CommentDTO> commentsOfThread;
    @NotNull
    private int postId;

    public CommentDTO(int id, int authorId, String comment, Date datetime, List<CommentDTO> commentsOfThread, int postId, int thread, String author) {
        this.id = id;
        this.authorId = authorId;
        this.comment = comment;
        this.datetime = datetime;
        this.commentsOfThread = commentsOfThread;
        this.postId = postId;
        this.thread = thread;
        this.author = author;
    }

    public CommentDTO(){}

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

    public List<CommentDTO> getCommentsOfThread() {
        return commentsOfThread;
    }

    public void setCommentsOfThread(List<CommentDTO> commentsOfThread) {
        this.commentsOfThread = commentsOfThread;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public CommentDTO dtoFromDomain(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment(comment.getComment());
        commentDTO.setDatetime(comment.getDatetime());
        commentDTO.setAuthorId(comment.getUser().getId());
        commentDTO.setId(comment.getId());
        commentDTO.setPostId(comment.getPost().getId());
        commentDTO.setAuthor(comment.getUser().getName()+" "+comment.getUser().getSurname());
        if(comment.getThread() == null){
            commentDTO.setThread(0);
        }
        else{
            commentDTO.setThread(comment.getThread().getId());
        }

        List<CommentDTO> commentsDTOOfThread = new ArrayList<>();
        if(comment.getCommentList() != null) {
            for (Comment comment1 : comment.getCommentList()) {
                CommentDTO commentDTO1 = new CommentDTO();
                commentsDTOOfThread.add(commentDTO1.dtoFromDomain(comment1));
            }
        }
        commentDTO.setCommentsOfThread(commentsDTOOfThread);
        return commentDTO;
    }
}
