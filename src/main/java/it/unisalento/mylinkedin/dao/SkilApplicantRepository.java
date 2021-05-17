package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.SkilApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkilApplicantRepository extends JpaRepository<SkilApplicant, Integer> {

    @Query("select sp from SkilApplicant  sp where sp.applicant.id =:userId")
    List<SkilApplicant> findByUserId(@Param("userId") int userId);
    @Query("select sp from SkilApplicant sp where sp.skil.id =:skilId")
    List<SkilApplicant> findBySkilId(@Param("skilId") int skilId);
}
