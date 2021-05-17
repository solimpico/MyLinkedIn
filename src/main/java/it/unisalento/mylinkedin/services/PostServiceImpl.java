package it.unisalento.mylinkedin.services;

import it.unisalento.mylinkedin.dao.*;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.DataDTO;
import it.unisalento.mylinkedin.exceptions.AddPostException;
import it.unisalento.mylinkedin.exceptions.PostException;
import it.unisalento.mylinkedin.exceptions.PostNotFoundException;
import it.unisalento.mylinkedin.iservices.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    DataRepository dataRepository;
    @Autowired
    SkilRepository skilRepository;
    @Autowired
    SkilPostRepository skilPostRepository;
    @Autowired
    OfferorRepository offerorRepository;

    @Override
    @Transactional(rollbackOn = PostException.class)
    public void deleteById(int id) throws PostException {
        try {
            postRepository.deleteById(id);
        }
        catch (Exception e){
            throw new PostException();
        }
        }

    @Override
    @Transactional
    public Post findById(int id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(()-> new PostNotFoundException());
    }

    @Override
    @Transactional
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    @Transactional
    public List<Post> findVisible(){
        return postRepository.findVisible();
    }

    @Override
    @Transactional(rollbackOn = AddPostException.class)
    public Post addPost(Post post, List<Data> dataList, List<String> skilList) throws AddPostException {
        try {
            //salvo il post (con info parziali) sul db per avere il riferimento per i data e per le skils
            post = postRepository.save(post);

            //salvo i data e avvaloro dataList
            for (Data data : dataList) {
                dataRepository.save(data);
            }
            post.setDataList(dataList);

            //salvo le associazioni post-skil
            List<SkilPost> skilPostList = new ArrayList<>();
            for (String skilName : skilList){
                Skil skil = skilRepository.findBySkilName(skilName);
                SkilPost skilPost = new SkilPost(0, skil, post);
                skilPost = skilPostRepository.save(skilPost);
                skilPostList.add(skilPost);
            }
            post.setSkilPostList(skilPostList);
            //salvo il post con tutte le informazioni
            post = postRepository.save(post);

            return post;
        }
        catch (Exception e){
            throw new AddPostException();
        }
    }

    @Override
    @Transactional
    public List<Post> findVisibleByDate(){
        return postRepository.findVisibileByDate();
    }

    @Override
    @Transactional
    public List<Post> findVisibleBySkil(int idSkil) {
        List<SkilPost> skilPostList = skilPostRepository.findBySkil(idSkil);
        List<Post> postList = new ArrayList<>();
        for (SkilPost skilPost : skilPostList){
            postList.add(skilPost.getPost());
        }
        return postList;
    }

    @Override
    @Transactional
    public List<Post> findVisibleByOfferor() {
        List<Offeror> offerorList = offerorRepository.findAll();
        List<Post> postList = new ArrayList<>();
        List<Post> postOfOfferor = new ArrayList<>();
        for (Offeror offeror : offerorList){
            postOfOfferor = postRepository.findVisibileByOfferor(offeror.getId());
            for (Post post : postOfOfferor){
                postList.add(post);
            }
        }
        return postList;
    }
}

/*
* Anche in questo caso come nel metodo addType di PostTypeServiceImpl è fondamentale aggiungere i dati
* ed il post nella stessa transazione affinché, in caso di errori tramite il rollback in automatico
* siamo in grado di eliminare ciò che è stato messo (evitando così di lasciare ad esempio
* un post senza alcun data)
* Non riusciremmo ad avere questo comportamento atomico se aggiungessimo "manualmente" i data
* e il post tramite i service nel metodo rest
* */

