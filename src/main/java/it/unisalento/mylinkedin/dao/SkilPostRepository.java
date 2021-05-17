package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.SkilPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkilPostRepository extends JpaRepository <SkilPost, Integer>{

    @Query("select sp from SkilPost sp where sp.skil.skilName like :skilName")
    List<SkilPost> findBySkilName(@Param("skilName") String skilName);

    @Query("select sp from SkilPost sp where sp.skil.id =:skilId")
    List<SkilPost> findBySkil(@Param("skilId") int skilId);
}
