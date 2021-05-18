package it.unisalento.mylinkedin.dao;

import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.Offeror;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfferorRepository extends JpaRepository<Offeror, Integer> {

    @Query("Select a from Offeror a where a.id =:id")
    Offeror getByUserId(@Param("id") int id);
    @Query("Select o from Offeror o where o.company.id =:idCompany")
    List<Offeror> getOfferorByCompanyId(@Param("idCompany") int idCompany);
    @Query("Select o from Offeror o where o.registered = false")
    List<Offeror> getNotRegisteredOfferor();
}
