package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.PostType;
import it.unisalento.mylinkedin.domain.RequiredField;
import it.unisalento.mylinkedin.domain.RequiredFieldPostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequiredFieldRepository extends JpaRepository<RequiredField, Integer> {

    @Query("Select r from RequiredField r where r.requiredField like :name")
    RequiredField findByName(@Param("name") String name);
}
