package it.unisalento.mylinkedin.dto;

import it.unisalento.mylinkedin.domain.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDTO {

    private int id;
    private Date datetime;
    private boolean visible;
    @NotNull
    private int userId;
    @NotNull
    private String type;
    @NotNull
    private List<DataDTO> dataDTOList;
    private List<CommentDTO> CommentDTOList;

    public PostDTO(){}

    public PostDTO(int id, Date datetime, boolean visible, int userId, String type, List<DataDTO> dataDTOList, List<CommentDTO> CommentDTOList) {
        this.id = id;
        this.datetime = datetime;
        this.visible = visible;
        this.userId = userId;
        this.type = type;
        this.dataDTOList = dataDTOList;
        this.CommentDTOList = CommentDTOList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DataDTO> getDataDTOList() {
        return dataDTOList;
    }

    public void setDataDTOList(List<DataDTO> dataDTOList) {
        this.dataDTOList = dataDTOList;
    }

    public List<CommentDTO> getCommentDTOList() {
        return CommentDTOList;
    }

    public void setCommentDTOList(List<CommentDTO> commentDTOList) {
        CommentDTOList = commentDTOList;
    }

    public PostDTO dtoFromDomain(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setDatetime(post.getPublicationDate());
        postDTO.setId(post.getId());
        postDTO.setVisible(post.isVisible());
        postDTO.setUserId(post.getUser().getId());
        postDTO.setType(post.getPostType().getType());

        List<CommentDTO> commentDTOList = new ArrayList<>();
        if(post.getCommentList() != null) {
            for (Comment comment : post.getCommentList()) {
                if (comment.getThread() == null) {
                    //È una thread allora cerco anche i suoi figli
                    commentDTOList.add(new CommentDTO().dtoFromDomain(comment));
                }
                //altrimenti nulla perché gli altri commenti o saranno thread o faranno parte di una thread
            }
        }
        postDTO.setCommentDTOList(commentDTOList);

        List<DataDTO> dataDTOList = new ArrayList<DataDTO>();
        for (Data data : post.getDataList()){
            DataDTO dataDTO = new DataDTO();
            dataDTOList.add(dataDTO.dtoFromDoman(data));
        }
        for (SkilPost skilPost : post.getSkilPostList()) {
            Skil skil = skilPost.getSkil();
            DataDTO dataDTO = new DataDTO(skil.getId(), "Skil", skil.getSkilName(), null);
            dataDTOList.add(dataDTO);

        }
        postDTO.setDataDTOList(dataDTOList);

        return postDTO;
    }
}
