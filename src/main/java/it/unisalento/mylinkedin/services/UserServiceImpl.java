package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.*;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.exceptions.ImageNotFoundException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IUserService;
import javassist.NotFoundException;
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
    ProfileImageRepository profileImageRepository;
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

    @Transactional
    public User isRegistered(String email) throws UserNotFoundException{
        User user = userRepository.findByEmail(email);
        if(user != null){
            return user;
        }
        throw new UserNotFoundException();
    }

    @Override
    @Transactional(rollbackOn = ImageNotFoundException.class)
    public User deleteProfileImage(int idUser) throws ImageNotFoundException {
        User user = userRepository.getOne(idUser);
        try {
            ProfileImage profileImage = profileImageRepository.findById(user.getProfileImage().getId()).orElseThrow(() -> new ImageNotFoundException());
            profileImage.setUser(null);
            profileImageRepository.delete(profileImage);
        }
        catch (Exception e){
            throw new ImageNotFoundException();
        }
        user.setProfileImage(null);
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public User addProfileImage(ProfileImage newProfileImage, int idUser){
        User user = userRepository.getOne(idUser);
        if(user.getProfileImage() != null){
            ProfileImage image = user.getProfileImage();
            image.setDescription(newProfileImage.getDescription());
            image.setProfilePicturePath(newProfileImage.getProfilePicturePath());
            profileImageRepository.save(image);
        }
        else {
            newProfileImage.setUser(user);
            user.setProfileImage(newProfileImage);
            profileImageRepository.save(newProfileImage);
        }
        return user;
    }
}
