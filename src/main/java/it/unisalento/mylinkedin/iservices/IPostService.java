package it.unisalento.mylinkedin.iservices;

import it.unisalento.mylinkedin.domain.Data;
import it.unisalento.mylinkedin.domain.Post;
import it.unisalento.mylinkedin.domain.SkilPost;
import it.unisalento.mylinkedin.exceptions.AddPostException;
import it.unisalento.mylinkedin.exceptions.PostException;
import it.unisalento.mylinkedin.exceptions.PostNotFoundException;

import java.util.List;

public interface IPostService {
    Post addPost(Post post, List<Data> dataList, List<String> skilList) throws AddPostException;
    void deleteById(int id) throws PostException;
    Post findById(int id) throws PostNotFoundException;
    List<Post> findAll();
    List<Post> findVisible();
    List<Post> findVisibleByDate();
    List<Post> findVisibleBySkil(int idSkil);
    List<Post> findVisibleByOfferor();
    List<Post> findVisibileByPosition(double latitudine, double longitudine);
    Post hidenShowPost(int id) throws PostNotFoundException;
}
