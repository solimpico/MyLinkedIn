package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.DataRepository;
import it.unisalento.mylinkedin.domain.Data;
import it.unisalento.mylinkedin.domain.Post;
import it.unisalento.mylinkedin.exceptions.AddPostException;
import it.unisalento.mylinkedin.iservices.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataServiceImpl implements IDataService {

   /* @Autowired
    DataRepository dataRepository;

    @Override
    @Transactional(rollbackOn = AddPostException.class)
    public Data addData(Data data) throws AddPostException {
        try {
            return dataRepository.save(data);
        } catch (Exception e) {
            throw new AddPostException();
        }
    }*/
}