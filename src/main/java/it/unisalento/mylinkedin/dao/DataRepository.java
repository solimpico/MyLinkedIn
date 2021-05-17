package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository <Data, Integer>{
}
