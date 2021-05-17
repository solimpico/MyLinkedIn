package it.unisalento.mylinkedin.iservices;

import it.unisalento.mylinkedin.domain.PostType;
import it.unisalento.mylinkedin.domain.RequiredField;
import it.unisalento.mylinkedin.exceptions.AddPostTypeException;
import it.unisalento.mylinkedin.exceptions.PostException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IPostTypeService {
    PostType addPostType(PostType postType, List<String> requiredFieldToAddList) throws AddPostTypeException;
    List<PostType> showAllPostType();
    PostType findByName(String name);
    void deletePostTypeById(int id) throws PostException;

}
