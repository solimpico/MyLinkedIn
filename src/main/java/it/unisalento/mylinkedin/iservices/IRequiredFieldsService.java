package it.unisalento.mylinkedin.iservices;

import it.unisalento.mylinkedin.domain.PostType;
import it.unisalento.mylinkedin.domain.RequiredField;
import it.unisalento.mylinkedin.exceptions.FieldNotFoundException;

import java.util.List;

public interface IRequiredFieldsService {
    List<RequiredField> getAll();
    List<RequiredField> findByType(int idType);
}
