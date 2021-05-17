package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("select p from Post p where p.visible = true")
    public List<Post> findVisible();
    @Query("select p from Post p where p.visible = true order by p.publicationDate desc")
    List<Post> findVisibileByDate();
    @Query("select p from Post p where p.user.id =:idOfferor")
    List<Post> findVisibileByOfferor(@Param("idOfferor") int idOfferor);
}
