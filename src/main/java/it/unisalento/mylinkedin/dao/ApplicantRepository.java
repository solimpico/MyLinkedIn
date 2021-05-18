package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {

    @Query("Select a from Applicant a where a.id =:id")
    Applicant getByUserId(@Param("id") int id);
    @Query("Select a from Applicant  a where a.registered = false")
    List<Applicant> getNotRegisteredApplicant();
}
