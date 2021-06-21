package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.CompanyRepository;
import it.unisalento.mylinkedin.dao.OfferorRepository;
import it.unisalento.mylinkedin.domain.Company;
import it.unisalento.mylinkedin.domain.Offeror;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.exceptions.CompanyException;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IOfferorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OfferorServiceImpl implements IOfferorService {

    @Autowired
    OfferorRepository offerorRepository;
    @Autowired
    CompanyRepository companyRepository;

    @Override
    @Transactional(rollbackOn = SavingUserException.class)
    public Offeror saveRegistrationtRequestOfferor(User user) throws SavingUserException {
        try{
            return offerorRepository.save(new Offeror(user.getId(), user.getName(), user.getSurname(),
                    user.getBirthday(), user.getAge(), user.getEmail(), user.getPassword(), user.getProfileImage(),
                    user.getNotificationList(), user.getMessageList(), user.getPostList(), user.getCommentList(), user.getSnsList(), false, false, null));

        }
        catch (Exception e){
            throw new SavingUserException();
        }
    }

    @Override
    @Transactional
    public Offeror findByUserId(int userId) throws UserNotFoundException {
        return offerorRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
    }

    @Override
    @Transactional(rollbackOn = DataIntegrityViolationException.class)
    public Offeror save(Offeror offeror) throws DataIntegrityViolationException {
        return offerorRepository.save(offeror);
    }

    @Override
    @Transactional(rollbackOn = CompanyException.class)
    public Company addCompany(Offeror offeror, Company company) throws CompanyException {
        try{
            offeror.setCompany(companyRepository.save(company));
            offerorRepository.save(offeror);
            return company;
        }
        catch (Exception e){
            throw new CompanyException();
        }
    }

    @Override
    @Transactional
    public Company findCompanyByName(String name){
        return companyRepository.findByName(name);
    }

    @Override
    @Transactional(rollbackOn = CompanyException.class)
    public void deleteCompany(int idCompany) throws CompanyException {
        try{
            List<Offeror> offerorList = offerorRepository.getOfferorByCompanyId(idCompany);
            for (Offeror offeror : offerorList){
                offeror.setCompany(null);
                offerorRepository.save(offeror);
            }
            companyRepository.deleteById(idCompany);
        }
        catch (Exception e){
            throw new CompanyException();
        }
    }

    @Override
    @Transactional
    public List<Offeror> findOfferorRegistrationRequest() {
        return offerorRepository.getNotRegisteredOfferor();
    }

    @Override
    public Offeror confirmAndEnable(int  idOfferor) throws UserNotFoundException {
        Offeror offeror = offerorRepository.findById(idOfferor).orElseThrow(() -> new UserNotFoundException());
        offeror.setEnabling(true);
        offeror.setRegistered(true);
        return offerorRepository.save(offeror);

    }
}
