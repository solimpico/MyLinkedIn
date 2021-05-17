package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.Skil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkilRepository extends JpaRepository <Skil, Integer>{
    Skil findBySkilName( String name);
}
