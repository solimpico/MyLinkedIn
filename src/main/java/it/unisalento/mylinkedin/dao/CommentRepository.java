package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository <Comment, Integer>{

    @Query("select c from Comment c where c.post.id =:idPost")
    List<Comment> getCommentsByIdPost(@Param("idPost")int idPost);
}
