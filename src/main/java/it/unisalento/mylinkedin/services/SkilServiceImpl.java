package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.SkilApplicantRepository;
import it.unisalento.mylinkedin.dao.SkilPostRepository;
import it.unisalento.mylinkedin.dao.SkilRepository;
import it.unisalento.mylinkedin.domain.Skil;
import it.unisalento.mylinkedin.domain.SkilApplicant;
import it.unisalento.mylinkedin.domain.SkilPost;
import it.unisalento.mylinkedin.exceptions.SkilException;
import it.unisalento.mylinkedin.exceptions.SkilNotFoundException;
import it.unisalento.mylinkedin.iservices.ISkilService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SkilServiceImpl implements ISkilService {

    @Autowired
    SkilRepository skilRepository;
    @Autowired
    SkilApplicantRepository skilApplicantRepository;
    @Autowired
    SkilPostRepository skilPostRepository;

    @Override
    @Transactional
    public Skil findById(int id) throws SkilNotFoundException {
        return skilRepository.findById(id).orElseThrow(() -> new SkilNotFoundException());
    }

    @Override
    @Transactional
    public void saveAssociation(SkilApplicant skilApplicant) {
        skilApplicantRepository.save(skilApplicant);
    }

    @Override
    @Transactional
    public List<Skil> findSkilByUserId(int id){
        List<SkilApplicant> skilApplicantList = skilApplicantRepository.findByUserId(id);
        List<Skil> skilList = new ArrayList<>();
        for (SkilApplicant skilApplicant : skilApplicantList){
            skilList.add(skilApplicant.getSkil());
        }
        return skilList;
    }

    @Override
    @Transactional(rollbackOn = SkilException.class)
    public Skil saveSkil(Skil skil) throws SkilException {
        try {
            return skilRepository.save(skil);
        }
        catch (Exception e){
            throw new SkilException();
        }
    }

    @Override
    @Transactional(rollbackOn = SkilNotFoundException.class)
    public void deleteSkil(int idSkil) throws SkilNotFoundException {
        try{
            List<SkilPost> skilPostList = skilPostRepository.findBySkil(idSkil);
            for(SkilPost skilPost : skilPostList){
                skilPostRepository.delete(skilPost);
            }
            List<SkilApplicant> skilApplicantList = skilApplicantRepository.findBySkilId(idSkil);
            for (SkilApplicant skilApplicant : skilApplicantList){
                skilApplicantRepository.delete(skilApplicant);
            }
            skilRepository.deleteById(idSkil);
        }
        catch (Exception e){
            new SkilNotFoundException();
        }
    }

    @Override
    @Transactional
    public List<Skil> findAll() {
        return skilRepository.findAll();
    }
}
