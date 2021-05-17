package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.PostTypeRepository;
import it.unisalento.mylinkedin.dao.RequiredFieldPostTypeRepository;
import it.unisalento.mylinkedin.dao.RequiredFieldRepository;
import it.unisalento.mylinkedin.domain.PostType;
import it.unisalento.mylinkedin.domain.RequiredField;
import it.unisalento.mylinkedin.domain.RequiredFieldPostType;
import it.unisalento.mylinkedin.exceptions.AddPostTypeException;
import it.unisalento.mylinkedin.exceptions.PostException;
import it.unisalento.mylinkedin.iservices.IPostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostTypeServiceImpl implements IPostTypeService {
    @Autowired
    PostTypeRepository postTypeRepository;
    @Autowired
    RequiredFieldRepository requiredFieldRepository;
    @Autowired
    RequiredFieldPostTypeRepository requiredFieldPostTypeRepository;
    
    @Override
    @Transactional(rollbackOn = AddPostTypeException.class)
    public PostType addPostType(PostType postType, List<String> requiredFieldToAddList) throws AddPostTypeException{
        try {
            /*Controllo per il postType esiste già
            List<PostType> postTypeList = postTypeRepository.findAll();
            for(PostType type : postTypeList){
                if(type.getType().equalsIgnoreCase(postType.getType())){
                    return null;
                }
            }*/
            boolean control;
            List<RequiredFieldPostType> requiredFieldPostTypeList = new ArrayList<RequiredFieldPostType>();
            //salvo postType (ha avvalorato solo il type) per avere il riferimo dell'id
            postTypeRepository.save(postType);
            //recupero la lista di tutti i campi obbligatori esistenti
            List<RequiredField> allRequiredFieldList = requiredFieldRepository.findAll();
            //per ogni campo obbligatori da collegare al type
            for (String reqFieldToAdd : requiredFieldToAddList) {
                //per ogni campo esistente
                control = false;
                RequiredField existingReqField = new RequiredField();
                for (RequiredField requiredField : allRequiredFieldList) {
                    //controllo se il campo da collegare esiste già
                    if(reqFieldToAdd.equalsIgnoreCase(requiredField.getRequiredField())){
                        control = true;
                        existingReqField = requiredField;
                    }
                }
                if(control == true){
                    //il campo esiste già
                    //definisco una nuova associazione e salvo
                    RequiredFieldPostType requiredFieldPostType = new RequiredFieldPostType(0, postType, existingReqField);
                    requiredFieldPostTypeRepository.save(requiredFieldPostType);
                    //avvaloro la lista delle associazioni
                    requiredFieldPostTypeList.add(requiredFieldPostType);
                }
                else {
                    //Il campo non esiste
                    //definisco e salvo il nuovo campo
                    RequiredField requiredFieldToSave = new RequiredField(0, reqFieldToAdd, null);
                    requiredFieldRepository.save(requiredFieldToSave);
                    //definisco una nuova associazione e salvo
                    RequiredFieldPostType requiredFieldPostType = new RequiredFieldPostType(0, postType, requiredFieldToSave);
                    requiredFieldPostTypeRepository.save(requiredFieldPostType);
                    //avvaloro la lista delle associazioni
                    requiredFieldPostTypeList.add(requiredFieldPostType);
                }
            }
            //definisco il postType da ritorna con i valori effettivi salvati nel db
            PostType postTypeSaved = new PostType(postType.getId(), postType.getType(), null, requiredFieldPostTypeList);
            return postTypeSaved;
        }
        catch (Exception e){
            throw new AddPostTypeException();
        }
    }

    @Override
    @Transactional
    public List<PostType> showAllPostType() {
        return  postTypeRepository.findAll();
    }

    @Override
    @Transactional
    public PostType findByName(String name) {
            return postTypeRepository.findByType(name);
    }

    @Override
    @Transactional(rollbackOn = PostException.class)
    public void deletePostTypeById(int id) throws PostException{
        postTypeRepository.findById(id).orElseThrow(() -> new PostException());
        postTypeRepository.deleteById(id);
    }
}

/*
* È fondamentale, all'interno del metodo addPostType salvare atomicamente
* tipologia-campi obbligatori-associazione affinché nel caso di errori il rollback possa
* essere eseguito correttamente.
* Se si fossero seperate le tre operazioni il rollback avrebbe agito singolarmente su esse ma non
* complessivamente (=> se ci fosse stato un errore nel salvataggio dei campi obbligaori questi
* sarebbero stati cancellati ma la nuova tipologia sarebbe rimasta salvata sul db).
*
* Il controllo sull'esistenza di una tipologia di post può essere fatto con i validator
* */
