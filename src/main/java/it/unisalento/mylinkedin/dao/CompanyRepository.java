package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("select c from Company c where c.name like :name")
    Company findByName(@Param("name") String name);

}
