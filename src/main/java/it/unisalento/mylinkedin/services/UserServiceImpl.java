package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.MessageRepository;
import it.unisalento.mylinkedin.dao.UserRepository;
import it.unisalento.mylinkedin.domain.Message;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.MessageException;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional(rollbackOn = UserNotFoundException.class)
    public void deleteUser(int id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User findById(int id) throws UserNotFoundException{
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    }

    @Override
    @Transactional
    public List<User> getAll(){
        return userRepository.findAll();
    }
}
