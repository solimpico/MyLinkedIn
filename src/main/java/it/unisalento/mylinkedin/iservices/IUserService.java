package it.unisalento.mylinkedin.iservices;

import it.unisalento.mylinkedin.domain.Message;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.MessageException;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;

import java.util.List;

public interface IUserService {

    void deleteUser(int id) throws UserNotFoundException;
    User findById(int id) throws UserNotFoundException;
    List<User> getAll();


}
