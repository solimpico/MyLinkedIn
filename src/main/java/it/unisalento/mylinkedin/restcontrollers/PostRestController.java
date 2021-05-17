package it.unisalento.mylinkedin.restcontrollers;

import it.unisalento.mylinkedin.dao.OfferorRepository;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.CommentDTO;
import it.unisalento.mylinkedin.dto.DataDTO;
import it.unisalento.mylinkedin.dto.PostDTO;
import it.unisalento.mylinkedin.exceptions.*;
import it.unisalento.mylinkedin.iservices.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Document;
import javax.validation.Valid;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostRestController {

    @Autowired
    IPostService postService;
    @Autowired
    IUserService userService;
    @Autowired
    IDataService dataService;
    @Autowired
    IPostTypeService postTypeService;
    @Autowired
    ICommentService commentService;
    @Autowired
    IApplicantService applicantService;
    @Autowired
    IOfferorService offerorService;


    @PostMapping(value = "/addPost", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO addPost(@RequestBody @Valid PostDTO postDTO) throws UserNotFoundException, AddPostException {
        Applicant applicant;
        Offeror offeror;
        boolean enable = false;
        try{
            applicant = applicantService.findByUserId(postDTO.getUserId());
            if(applicant.isEnabling() && applicant.isRegistered()){
                enable = true;
            }
        }
        catch (Exception e){}
        try{
            offeror = offerorService.findByUserId(postDTO.getUserId());
            if(offeror.isEnabling() && offeror.isRegistered()){
                enable = true;
            }
        }
        catch (Exception e){}
        if(enable == true) {
            Post post = new Post(postDTO.getId(), true, new Date(), userService.findById(postDTO.getUserId()), null, null, postTypeService.findByName(postDTO.getType()), null, null);
            List<Data> dataList = new ArrayList<>();
            List<String> skilList = new ArrayList<>();
            //avvaloro dataList e/o skilList
            for (DataDTO dataDTO : postDTO.getDataDTOList()) {
                if (dataDTO.getField().equalsIgnoreCase("skil")) {
                    skilList.add(dataDTO.getData());
                } else {
                    Data data = new Data(dataDTO.getId(), dataDTO.getField(), dataDTO.getData(), dataDTO.getDataFilePath(), post);
                    dataList.add(data);
                }
            }
            post = postService.addPost(post, dataList, skilList);

            return postDTO.dtoFromDomain(post);
        }
        else{
            System.out.println("Tentativo di aggiunta post da parte di utente non registrato\no non abilitato.");
            return null;
        }
    }

    @DeleteMapping (value = ("/delete/{id}"), produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> deletePost(@PathVariable int id) throws PostException{
        postService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/getById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO showPost(@PathVariable  int id) throws PostNotFoundException {
        return new PostDTO().dtoFromDomain(postService.findById(id));
    }

   /* @GetMapping(value = "/showVisible", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDTO> showVisible(){
        List<PostDTO> postDTOList = new ArrayList<>();
        List<Post> postList = postService.findVisible();
        for (Post post : postList) {
            PostDTO postDTO = new PostDTO();
            postDTOList.add(postDTO.dtoFromDomain(post));
        }
        return postDTOList;
    }*/

    @PostMapping(value = "/addComment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO addComment(@RequestBody @Valid CommentDTO commentDTO) throws PostNotFoundException, CommentNotFoundException {
        Post post = postService.findById(commentDTO.getPostId());
        if(commentDTO.getThread() == 0){
            //nuova thread
            commentService.save(new Comment(0, commentDTO.getAuthor(), commentDTO.getComment(), new Date(), null, null, post));
            //ritorno il post aggiornato
            return new PostDTO().dtoFromDomain(post);
        }
        else {
            //thread esistente
            //trovo il commento thread (per mezzo dell' id)
            Comment thread = commentService.findById(commentDTO.getThread());
            //creo il commento
            Comment comment = new Comment(0, commentDTO.getAuthor(), commentDTO.getComment(), new Date(), thread, null, post);
            //lo salvo per avere l'id
            commentService.save(comment);
            return new PostDTO().dtoFromDomain(post);

        }
    }

    @DeleteMapping(value = "/removeComment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO deleteComment(@PathVariable int id) throws PostNotFoundException, CommentNotFoundException{
        int idPost = commentService.findById(id).getPost().getId();
        commentService.deleteById(id);
        return new PostDTO().dtoFromDomain(postService.findById(idPost));
    }

    @GetMapping(value = "/showVisibleByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDTO> showVisibleByDate(){
        List<PostDTO> postDTOList = new ArrayList<>();
        List<Post> postList = postService.findVisibleByDate();
        for (Post post : postList) {
            PostDTO postDTO = new PostDTO();
            postDTOList.add(postDTO.dtoFromDomain(post));
        }
        return postDTOList;
    }
}
