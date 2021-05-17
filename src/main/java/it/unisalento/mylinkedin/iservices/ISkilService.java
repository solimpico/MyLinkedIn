package it.unisalento.mylinkedin.iservices;

import it.unisalento.mylinkedin.domain.Skil;
import it.unisalento.mylinkedin.domain.SkilApplicant;
import it.unisalento.mylinkedin.exceptions.SkilException;
import it.unisalento.mylinkedin.exceptions.SkilNotFoundException;
import javassist.NotFoundException;

import java.util.List;

public interface ISkilService {
    Skil findById(int id) throws SkilNotFoundException;
    void saveAssociation(SkilApplicant skilApplicant);
    List<Skil> findSkilByUserId(int id);
    Skil saveSkil(Skil skil) throws SkilException;
    void deleteSkil(int idSkil) throws SkilNotFoundException;
    List<Skil> findAll();
}
