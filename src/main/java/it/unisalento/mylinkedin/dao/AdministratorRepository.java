package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

}
