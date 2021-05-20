package it.unisalento.mylinkedin.iservices;

import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.exceptions.CompanyException;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface IOfferorService{
    Offeror saveRegistrationtRequestOfferor(User user) throws SavingUserException;
    Offeror findByUserId(int userId) throws UserNotFoundException;
    Offeror save(Offeror offeror) throws DataIntegrityViolationException;
    Company addCompany(Offeror offeror, Company company) throws CompanyException;
    Company findCompanyByName(String name);
    void deleteCompany(int idCompany) throws  CompanyException;
    List<Offeror> findOfferorRegistrationRequest();
    Offeror confirmAndEnable(int  idOfferor) throws UserNotFoundException;

}
