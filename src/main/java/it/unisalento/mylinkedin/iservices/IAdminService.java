package it.unisalento.mylinkedin.iservices;


import it.unisalento.mylinkedin.domain.Administrator;
import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;

public interface IAdminService {
    Administrator saveRegistrationRequestAdmin(User user) throws SavingUserException;
    Administrator findByUserId(int userId) throws UserNotFoundException;
}
