package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.AdministratorRepository;
import it.unisalento.mylinkedin.dao.ApplicantRepository;
import it.unisalento.mylinkedin.dao.OfferorRepository;
import it.unisalento.mylinkedin.dao.UserRepository;
import it.unisalento.mylinkedin.domain.Administrator;
import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.Offeror;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    OfferorRepository offerorRepository;
    @Autowired
    AdministratorRepository adminRepository;

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

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public String whoIs(User user) throws UserNotFoundException {
        String role = "admin";
        Offeror offeror = offerorRepository.getOne(user.getId());
        if(offeror != null){
            role = "offeror";
            return role;
        }
        Applicant applicant = applicantRepository.getOne(user.getId());
        if(applicant != null){
            role = "applicant";
            return role;
        }
        Administrator admin = adminRepository.getOne(user.getId());
        if(admin != null){
            role = "admin";
            return role;
        }
        throw new UserNotFoundException();
    }

    public User isRegistered(String email) throws UserNotFoundException{
        User user = userRepository.findByEmail(email);
        if(user != null){
            return user;
        }
        throw new UserNotFoundException();
    }
}
