package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.ApplicantRepository;
import it.unisalento.mylinkedin.domain.Administrator;
import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ApplicantServiceImpl implements IApplicantService {

    @Autowired
    ApplicantRepository applicantRepository;

    @Override
    @Transactional(rollbackOn = SavingUserException.class)
    public Applicant saveRegistrationtRequestApplicant(User user) throws SavingUserException{
        try{
            return applicantRepository.save(new Applicant(user.getId(), user.getName(), user.getSurname(),
                    user.getBirthday(), user.getAge(), user.getEmail(), user.getPassword(), user.getProfileImage(),
                    user.getNotificationList(), user.getMessageList(), user.getPostList(), false, false, null));

        }
        catch (Exception e){
            throw new SavingUserException();
        }

    }

    @Override
    @Transactional
    public Applicant findByUserId(int userId) throws UserNotFoundException {
        return applicantRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
    }

    @Override
    @Transactional(rollbackOn = DataIntegrityViolationException.class)
    public Applicant save(Applicant applicant) throws DataIntegrityViolationException {
         return applicantRepository.save(applicant);
    }

    @Override
    @Transactional
    public List<Applicant> findApplicantRegistrationRequest() {
        return applicantRepository.getNotRegisteredApplicant();
    }
}
