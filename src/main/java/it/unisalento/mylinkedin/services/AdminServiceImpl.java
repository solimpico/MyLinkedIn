package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.AdministratorRepository;
import it.unisalento.mylinkedin.domain.Administrator;
import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    AdministratorRepository administratorRepository;

    @Override
    @Transactional(rollbackOn = SavingUserException.class)
    public Administrator saveRegistrationRequestAdmin(User user) throws SavingUserException {
        try{
            return administratorRepository.save(new Administrator(user.getId(), user.getName(), user.getSurname(),
                    user.getBirthday(), user.getAge(), user.getEmail(), user.getPassword(), user.getProfileImage(),
                    user.getNotificationList(), user.getMessageList(), user.getPostList()));

        }
        catch (Exception e){
            throw new SavingUserException();
        }
    }

    @Override
    @Transactional
    public Administrator findByUserId(int userId) throws UserNotFoundException {
        try {
            return administratorRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        }
        catch (Exception e){
            throw new UserNotFoundException();
        }
    }
}
