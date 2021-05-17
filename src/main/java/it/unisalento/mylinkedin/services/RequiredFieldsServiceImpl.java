package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.PostTypeRepository;
import it.unisalento.mylinkedin.dao.RequiredFieldPostTypeRepository;
import it.unisalento.mylinkedin.dao.RequiredFieldRepository;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.exceptions.FieldNotFoundException;
import it.unisalento.mylinkedin.iservices.IRequiredFieldsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequiredFieldsServiceImpl implements IRequiredFieldsService {

    @Autowired
    RequiredFieldRepository requiredFieldRepository;
    @Autowired
    RequiredFieldPostTypeRepository requiredFieldPostTypeRepository;
    @Autowired
    PostTypeRepository postTypeRepository;

    @Override
    @Transactional
    public List<RequiredField> getAll() {
        return requiredFieldRepository.findAll();
    }

    @Override
    @Transactional
    public List<RequiredField> findByType(int idType)  {
        List<RequiredFieldPostType> requiredFieldPostTypeList = requiredFieldPostTypeRepository.findByIdType(idType);
        List<RequiredField> requiredFieldList = new ArrayList<>();
        for (RequiredFieldPostType requiredFieldPostType : requiredFieldPostTypeList){
            requiredFieldList.add(requiredFieldPostType.getRequiredField());
        }
        return requiredFieldList;
    }
}
