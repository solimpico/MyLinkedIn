package it.unisalento.mylinkedin.iservices;

import it.unisalento.mylinkedin.domain.Administrator;
import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;

public interface IApplicantService {
    Applicant saveRegistrationtRequestApplicant(User user) throws SavingUserException;
    Applicant findByUserId(int userId) throws UserNotFoundException;
    Applicant save(Applicant applicant) throws DataIntegrityViolationException;

}
