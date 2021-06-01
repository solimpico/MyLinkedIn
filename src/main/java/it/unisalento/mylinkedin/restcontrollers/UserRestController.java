package it.unisalento.mylinkedin.restcontrollers;

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
    IPostTypeService postTypeService;
    @Autowired
    JwtProvider jwtProvider;


    @PostMapping(value="/public/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginTokenDTO> authenticate(@RequestBody LoginInputDTO body) throws UserNotFoundException{
        User user = userService.findByEmail(body.getEmail());
        LoginTokenDTO tokenDTO = new LoginTokenDTO();
        if(user.getClass().equals(Applicant.class)) {
            if (((Applicant) user).isRegistered() == true && ((Applicant) user).isEnabling() == true
                    && user != null && user.getEmail().equals(body.getEmail())
                    && user.getPassword().equals(body.getPassword())) {
                    //param role
                    String jwt = jwtProvider.createJwt(user.getEmail(), user.getClass().getName());
                    tokenDTO.setToken(jwt);
                    tokenDTO.setUserId(user.getId());
                    return ResponseEntity.ok(tokenDTO);
            }
        }
        else if(user.getClass().equals(Offeror.class)) {
            if (((Offeror) user).isRegistered() == true && ((Offeror) user).isEnabling() == true
                    && user != null && user.getEmail().equals(body.getEmail())
                    && user.getPassword().equals(body.getPassword())) {
                    //param role
                    String jwt = jwtProvider.createJwt(user.getEmail(), user.getClass().getName());
                    tokenDTO.setToken(jwt);
                    tokenDTO.setUserId(user.getId());
                    return ResponseEntity.ok(tokenDTO);
            }
        }
            else if(user.getClass().equals(Administrator.class)
                && user != null && user.getEmail().equals(body.getEmail())
                && user.getPassword().equals(body.getPassword())){
                    //param role
                    String jwt = jwtProvider.createJwt(user.getEmail(), user.getClass().getName());
                    tokenDTO.setToken(jwt);
                    tokenDTO.setUserId(user.getId());
                    return ResponseEntity.ok(tokenDTO);
        }
            return ResponseEntity.ok(tokenDTO);
    }


    @PostMapping(value = "/public/registrationRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO registrationRequest(@RequestBody UserDTO userDTO) throws SavingUserException, SkilNotFoundException {
        User user = new User(0, userDTO.getName(), userDTO.getSurname(), userDTO.getBirthday(), userDTO.getAge(), userDTO.getEmail(), userDTO.getPassword(), null, null, null, null, null);
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

    @GetMapping(value = "/public/getPostById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO showPost(@PathVariable  int id) throws PostNotFoundException {
        return new PostDTO().dtoFromDomain(postService.findById(id));
    }

    @PostMapping(value = "/addProfileImage", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProfileImageDTO addProfileImage(@RequestBody @Valid ProfileImageDTO profileImageDTO, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException, ImageNotFoundException{
        int idUser = isRegistered(request);
        if(idUser != 0){
            User us = userService.addProfileImage
                    (new ProfileImage(0, profileImageDTO.getDescription(),
                            profileImageDTO.getPath(), null), idUser);
                return new ProfileImageDTO().DTOFromDomain(us.getProfileImage());
        }
        else throw new UserNotAuthorizedException();
    }

    @DeleteMapping(value = "/deleteProfileImage", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO deleteProfileImage(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException, ImageNotFoundException{
        int idUser = isRegistered(request);
        if(idUser != 0){
            return new UserDTO().getUserDTOFromDomain(userService.deleteProfileImage(idUser));
        }
        else{
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value = "/getProfileImage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfileImageDTO getProfileImage(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException, ImageNotFoundException{
        int idUser = isRegistered(request);
        if(idUser != 0){
            return new ProfileImageDTO().DTOFromDomain(userService.findById(idUser).getProfileImage());
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @PostMapping(value = "/sendMessage", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MessageDTO sendMessage(@RequestBody @Valid MessageDTO messageDTO, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, MessageException, UserNotAuthorizedException {
        int idUser = isRegistered(request);
        if(idUser != 0){
            User sender = userService.findById(idUser);
            User receiver = userService.findById(messageDTO.getIdReceiver());
            if(messageDTO.getConversationId() == 0){
                //nuova conversazione
                return new MessageDTO().dtoFromDomain(messageService.saveMessage(new Message(0, messageDTO.getMessage(), new Date(), messageDTO.getIdReceiver(), sender, null, null)), receiver.getName()+" "+receiver.getSurname());
            }
            else {
                //conversazione esistente
                //trovo il messaggio di riferimento
                Message conversation = messageService.findById(messageDTO.getConversationId());
                //creo il messaggio
                Message mess = new Message(0, messageDTO.getMessage(), new Date(), messageDTO.getIdReceiver(), sender, conversation, null);
                mess = messageService.saveMessage(mess);
                return new MessageDTO().dtoFromDomain(mess, receiver.getName()+" "+receiver.getSurname());
            }
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value = "/showConversation", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageDTO> showConversation(HttpServletRequest request, HttpServletResponse response) throws MessageException, UserNotFoundException, UserNotAuthorizedException{
        int idUser = isRegistered(request);
        if(idUser != 0) {
            List<Message> messageList = messageService.getMessageByUserId(idUser);
            List<MessageDTO> messageDTOList = new ArrayList<>();
            for (Message message : messageList) {
                User receiver = userService.findById(message.getIdReceiver());
                messageDTOList.add(new MessageDTO().dtoFromDomain(message, receiver.getName()+" "+receiver.getSurname()));
            }
            return messageDTOList;
        }
            else {
                throw new UserNotAuthorizedException();
            }
    }

    @DeleteMapping(value = "/deleteUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "public/getUserById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO showUser(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{

            User user = userService.findById(id);
            if(user.getClass() == Applicant.class){
                return new ApplicantDTO().dtoFromDomain((Applicant) user);
            }
            else if (user.getClass() == Offeror.class){
                return new OfferorDTO().dtoFromDomain((Offeror) user);
            } else if ( user.getClass() == Administrator.class){
                return new UserDTO().getUserDTOFromDomain(user);
            } else {
                throw new UserNotFoundException();
            }
    }

    @PutMapping(value = "/updateAge/{age}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO updateAgeUser(@PathVariable int age, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        int idUser = isRegistered(request);
        if(idUser != 0){
            return new UserDTO().getUserDTOFromDomain(userService.updateAge(idUser, age));
        }
        else { throw new UserNotAuthorizedException();}
    }

    @GetMapping(value = "/showExistingType", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostTypeDTO> showType(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isRegistered(request) != 0){
            List<PostTypeDTO> postTypeDTOList = new ArrayList<>();
            List<PostType> postTypeList =  postTypeService.showAllPostType();
            for (PostType type : postTypeList){
                postTypeDTOList.add(new PostTypeDTO().dtoFromDomain(type));
            }
            return postTypeDTOList;
        }
        else{
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value = "/showAllSkils", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SkilDTO> showAllSkil(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isRegistered(request) != 0){
            List<Skil> skilList = skilService.findAll();
            List<SkilDTO> skilDTOList = new ArrayList<>();
            for (Skil skil : skilList){
                skilDTOList.add(new SkilDTO().dtoFromDomain(skil));
            }
            return skilDTOList;
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value="/getAllUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getAllUsers(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isRegistered(request) != 0){
            List<User> userList = userService.getAll();
            List<UserDTO> userDTOList = new ArrayList<>();
            for(User user : userList){
                if(user.getClass() == Applicant.class) {
                    userDTOList.add(new ApplicantDTO().dtoFromDomain((Applicant) user));
                } else if (user.getClass() == Offeror.class){
                    userDTOList.add(new OfferorDTO().dtoFromDomain((Offeror) user));
                } else {
                    userDTOList.add(new UserDTO().getUserDTOFromDomain(user));
                }
            }
            return userDTOList;
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
