package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.RequiredFieldPostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequiredFieldPostTypeRepository extends JpaRepository <RequiredFieldPostType, Integer>{
    @Query("select rfpt from RequiredFieldPostType  rfpt where rfpt.postType.id =:typeId")
    List<RequiredFieldPostType> findByIdType(@Param("typeId")int typeId);
}
