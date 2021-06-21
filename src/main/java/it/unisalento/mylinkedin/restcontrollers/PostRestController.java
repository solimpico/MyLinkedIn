package it.unisalento.mylinkedin.restcontrollers;

import it.unisalento.mylinkedin.dao.OfferorRepository;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.CommentDTO;
import it.unisalento.mylinkedin.dto.DataDTO;
import it.unisalento.mylinkedin.dto.PositionDTO;
import it.unisalento.mylinkedin.dto.PostDTO;
import it.unisalento.mylinkedin.exceptions.*;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    JwtProvider jwtProvider;


    @PostMapping(value = "/addPost", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO addPost(@RequestBody @Valid PostDTO postDTO, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, AddPostException, UserNotAuthorizedException {
        if (isOfferorOrApplicant(request) != 0) {
            boolean enable = false;
            User user = userService.findById(postDTO.getUserId());
            if (user.getClass() == Applicant.class && ((Applicant) user).isRegistered() && ((Applicant) user).isEnabling()) {
                Post post = new Post(postDTO.getId(), true, new Date(), userService.findById(postDTO.getUserId()),null, null, postTypeService.findByName(postDTO.getType()), null, null);
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
            } else if (user.getClass() == Offeror.class && ((Offeror) user).isRegistered() && ((Offeror) user).isEnabling()) {
                Post post = new Post(postDTO.getId(), true, new Date(), userService.findById(postDTO.getUserId()), null,null, postTypeService.findByName(postDTO.getType()), null, null);
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
            else {
                throw new UserNotAuthorizedException();
            }
        }
            else {
                throw new UserNotAuthorizedException();
            }
    }

    @DeleteMapping (value = ("/delete/{id}"), produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> deletePost(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws PostNotFoundException, PostException, UserNotAuthorizedException, UserNotFoundException{
        int idUser = isOfferorOrApplicant(request);
        if(idUser != 0){
            Post post = postService.findById(id);
            if(post.getUser().getId() == idUser) {
                postService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                throw new UserNotAuthorizedException();
            }
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @PostMapping(value = "/addComment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO addComment(@RequestBody @Valid CommentDTO commentDTO, HttpServletRequest request, HttpServletResponse response) throws PostNotFoundException, CommentNotFoundException, UserNotFoundException, UserNotAuthorizedException {
        int idUser = isOfferorOrApplicant(request);
        if(idUser != 0){
                Post post = postService.findById(commentDTO.getPostId());
                User author = userService.findById(idUser);
                if(commentDTO.getThread() == 0){
                    //nuova thread
                    commentService.save(new Comment(0, author, commentDTO.getComment(), new Date(), null, null, post));
                    //ritorno il post aggiornato
                    return new PostDTO().dtoFromDomain(post);
                }
                else {
                    //thread esistente
                    //trovo il commento thread (per mezzo dell' id)
                    Comment thread = commentService.findById(commentDTO.getThread());
                    //creo il commento
                    Comment comment = new Comment(0, author, commentDTO.getComment(), new Date(), thread, null, post);
                    //lo salvo per avere l'id
                    commentService.save(comment);
                    return new PostDTO().dtoFromDomain(post);
                }
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @DeleteMapping(value = "/removeComment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO deleteComment(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws PostNotFoundException, CommentNotFoundException, UserNotFoundException, UserNotAuthorizedException{
        int userId = isOfferorOrApplicant(request);
        if(userId != 0){
            int idPost = commentService.findById(id).getPost().getId();
            int idAuthor = commentService.findById(id).getUser().getId();
            if(idAuthor == userId) {
                commentService.deleteById(id);
                return new PostDTO().dtoFromDomain(postService.findById(idPost));
            }
            else {
                throw new UserNotAuthorizedException();
            }
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value = "/showVisibleByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDTO> showVisibleByDate(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isOfferorOrApplicant(request) != 0){
            List<PostDTO> postDTOList = new ArrayList<>();
            List<Post> postList = postService.findVisibleByDate();
            for (Post post : postList) {
                PostDTO postDTO = new PostDTO();
                postDTOList.add(postDTO.dtoFromDomain(post));
            }
            return postDTOList;
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @PostMapping(value = "/showByPosition", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDTO> finPostByPosition(@RequestBody @Valid PositionDTO positionDTO){
        List<Post> postList = postService.findVisibileByPosition(positionDTO.getLatitudine(), positionDTO.getLongitudine());
        List<PostDTO> postDTOList = new ArrayList<>();
        for(Post post: postList){
            postDTOList.add(new PostDTO().dtoFromDomain(post));
        }
        return postDTOList;
    }

    //CONTROL
    private int isOfferorOrApplicant(HttpServletRequest request) throws UserNotFoundException{
        String token = request.getHeader("Authorization").replace("Bearer ","");
        String email  = jwtProvider.decodeJwt(token).getSubject();
        User user = userService.isRegistered(email);
        if(user.getClass().equals(Offeror.class) || user.getClass().equals(Applicant.class)){
            return user.getId();
        }
        return 0;
    }
}
