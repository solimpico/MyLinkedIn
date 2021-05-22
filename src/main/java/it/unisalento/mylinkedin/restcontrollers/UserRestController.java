package it.unisalento.mylinkedin.restcontrollers;

import it.unisalento.mylinkedin.dao.UserRepository;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.*;
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
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class UserRestController {

    @Autowired
    IUserService userService;
    @Autowired
    IApplicantService applicantService;
    @Autowired
    IOfferorService offerorService;
    @Autowired
    IAdminService adminService;
    @Autowired
    IMessageService messageService;
    @Autowired
    ISkilService skilService;
    @Autowired
    IPostService postService;

    @Autowired
    JwtProvider jwtProvider;


    @PostMapping(value="/public/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginTokenDTO> authenticate(@RequestBody LoginInputDTO body) throws UserNotFoundException{
        User user = userService.findByEmail(body.getEmail());
        LoginTokenDTO tokenDTO = new LoginTokenDTO();
        if(user.getClass().equals(Applicant.class)) {
            if (((Applicant) user).isRegistered() == true && ((Applicant) user).isEnabling() == true) {
                if (user != null && user.getEmail().equals(body.getEmail()) && user.getPassword().equals(body.getPassword())) {
                    //param role
                    String jwt = jwtProvider.createJwt(user.getEmail(), user.getClass().getName());
                    tokenDTO.setToken(jwt);
                    return ResponseEntity.ok(tokenDTO);
                }
            }
        }
        else if(user.getClass().equals(Offeror.class)) {
            if (((Offeror) user).isRegistered() == true && ((Offeror) user).isEnabling() == true) {
                if (user != null && user.getEmail().equals(body.getEmail()) && user.getPassword().equals(body.getPassword())) {
                    //param role
                    String jwt = jwtProvider.createJwt(user.getEmail(), user.getClass().getName());
                    tokenDTO.setToken(jwt);
                    return ResponseEntity.ok(tokenDTO);
                }
            }
        }
            else if(user.getClass().equals(Administrator.class)){
                if(user != null && user.getEmail().equals(body.getEmail()) && user.getPassword().equals(body.getPassword())) {
                    //param role
                    String jwt = jwtProvider.createJwt(user.getEmail(), user.getClass().getName());
                    tokenDTO.setToken(jwt);
                    return ResponseEntity.ok(tokenDTO);
            }
        }
            return ResponseEntity.ok(tokenDTO);
    }


    @PostMapping(value = "/public/registrationRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO registrationRequest(@RequestBody @Valid UserDTO userDTO) throws SavingUserException, SkilNotFoundException {
        User user = new User(0, userDTO.getName(), userDTO.getSurname(), userDTO.getBirthday(), userDTO.getAge(), userDTO.getEmail(), userDTO.getPassword(), null, null, null, null);
        if(userDTO.getRole().equalsIgnoreCase("offeror")){
            //salva la richiesta di un offeror
            return new OfferorDTO().dtoFromDomain(offerorService.saveRegistrationtRequestOfferor(user));
        }
        else if (userDTO.getRole().equalsIgnoreCase("applicant")){
            return new ApplicantDTO().dtoFromDomain(applicantService.saveRegistrationtRequestApplicant(user));

        }
        else if(userDTO.getRole().equalsIgnoreCase("admin")){
            return new UserDTO().getUserDTOFromDomain(adminService.saveRegistrationRequestAdmin(user));
        }
        return null;
        }

    @GetMapping(value = "/public/showVisible", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDTO> showVisible(){
        List<PostDTO> postDTOList = new ArrayList<>();
        List<Post> postList = postService.findVisible();
        for (Post post : postList) {
            PostDTO postDTO = new PostDTO();
            postDTOList.add(postDTO.dtoFromDomain(post));
        }
        return postDTOList;
    }

    @GetMapping(value = "public/getById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO showPost(@PathVariable  int id) throws PostNotFoundException {
        return new PostDTO().dtoFromDomain(postService.findById(id));
    }

    @PostMapping(value = "/sendMessage", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MessageDTO sendMessage(@RequestBody @Valid MessageDTO messageDTO, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, MessageException, UserNotAuthorizedException {
        if(isRegistered(request) != 0){
            User sender = userService.findById(messageDTO.getIdSender());
            if(messageDTO.getConversationId() == 0){
                //nuova conversazione
                return new MessageDTO().dtoFromDomain(messageService.saveMessage(new Message(0, messageDTO.getMessage(), new Date(), messageDTO.getIdReceiver(), sender, null, null)));
            }
            else {
                //conversazione esistente
                //trovo il messaggio di riferimento
                Message conversation = messageService.findById(messageDTO.getConversationId());
                //creo il messaggio
                Message mess = new Message(0, messageDTO.getMessage(), new Date(), messageDTO.getIdReceiver(), sender, conversation, null);
                mess = messageService.saveMessage(mess);
                return new MessageDTO().dtoFromDomain(mess);
            }
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value = "showConversation/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageDTO> showConversation(@PathVariable int userId, HttpServletRequest request, HttpServletResponse response) throws MessageException, UserNotFoundException, UserNotAuthorizedException{
        int idUser = isRegistered(request);
        if(idUser != 0){
            if(idUser == userId){
                List<Message> messageList = messageService.getMessageByUserId(userId);
                List<MessageDTO> messageDTOList = new ArrayList<>();
                for (Message message : messageList){
                    messageDTOList.add(new MessageDTO().dtoFromDomain(message));
                }
                return messageDTOList;
            }
            else {
                throw new UserNotAuthorizedException();
            }
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deleteUser(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        int idUser = isRegistered(request);
        if(idUser != 0){
            if(idUser == id) {
                userService.deleteUser(id);
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

    @GetMapping(value = "/getById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO showUser(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isRegistered(request) != 0){
            if(applicantService.findByUserId(id) != null){
                return new ApplicantDTO().dtoFromDomain(applicantService.findByUserId(id));
            }
            else if (offerorService.findByUserId(id) != null){
                return new OfferorDTO().dtoFromDomain(offerorService.findByUserId(id));
            }
            return null;
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    //CONTROL
    private int isRegistered(HttpServletRequest request) throws UserNotFoundException{
        String token = request.getHeader("Authorization").replace("Bearer ","");
        String email  = jwtProvider.decodeJwt(token).getSubject();
        User user = userService.isRegistered(email);
        if(user != null){
            return user.getId();
        }
        return 0;
    }

}
