package it.unisalento.mylinkedin.restcontrollers;

import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.*;
import it.unisalento.mylinkedin.exceptions.*;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.security.JwtProvider;
import it.unisalento.mylinkedin.utility.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;

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
    INotificationService notificationService;
    @Autowired
    ISnsService snsService;
    @Autowired
    JwtProvider jwtProvider;


    @PostMapping(value="/public/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginTokenDTO> authenticate(@RequestBody LoginInputDTO body) throws UserNotFoundException{
        User user = new User();
        LoginTokenDTO tokenDTO = new LoginTokenDTO();
        try {
            user = userService.findByEmail(body.getEmail());
        } catch (UserNotFoundException e){
            return ResponseEntity.ok(tokenDTO);
        }
        if (user.getClass().equals(Applicant.class)) {
            if (((Applicant) user).isRegistered() == true && ((Applicant) user).isEnabling() == true
                    && user != null && user.getEmail().equals(body.getEmail())
                    && user.getPassword().equals(body.getPassword())) {
                //param role
                String jwt = jwtProvider.createJwt(user.getEmail(), user.getClass().getName());
                tokenDTO.setToken(jwt);
                tokenDTO.setUserId(user.getId());
                return ResponseEntity.ok(tokenDTO);
            }
        } else if (user.getClass().equals(Offeror.class)) {
            if (((Offeror) user).isRegistered() == true && ((Offeror) user).isEnabling() == true
                    && user != null && user.getEmail().equals(body.getEmail())
                    && user.getPassword().equals(body.getPassword())) {
                //param role
                String jwt = jwtProvider.createJwt(user.getEmail(), user.getClass().getName());
                tokenDTO.setToken(jwt);
                tokenDTO.setUserId(user.getId());
                return ResponseEntity.ok(tokenDTO);
            }
        } else if (user.getClass().equals(Administrator.class)
                && user != null && user.getEmail().equals(body.getEmail())
                && user.getPassword().equals(body.getPassword())) {
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
        User user = new User(0, userDTO.getName(), userDTO.getSurname(), userDTO.getBirthday(), userDTO.getAge(), userDTO.getEmail(), userDTO.getPassword(), null, null, null, null, null, null);
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

    @GetMapping(value = "getAllNotification", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NotificationDTO> getAllNotification(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        int idUser = isRegistered(request);
        if(idUser != 0){
            List<Notification> notifications = notificationService.getNotificationsByUserId(idUser);
            List<NotificationDTO> notificationDTOS = new ArrayList<>();
            for (Notification not: notifications){
                notificationDTOS.add(new NotificationDTO().dtoFromDomain(not));
            }
            return notificationDTOS;
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @DeleteMapping(value = "deleteNotification/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notification> deleteNotification(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        int idUser = isRegistered(request);
        if(idUser != 0) {
            notificationService.deleteNotification(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @DeleteMapping(value = "deleteAllnotificationOfUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notification> deleteAllNotification(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        int idUser = isRegistered(request);
        if(idUser != 0){
            notificationService.deleteAllNotificationByUserId(idUser);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @PostMapping(value = "addNotification", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public NotificationDTO addNotification(@RequestBody @Valid NotificationDTO notificationDTO,HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException, PostNotFoundException{
        int idUser = isRegistered(request);
        if(idUser != 0){
            User user = userService.findById(notificationDTO.getUserDTO().getId());
            Post post = new Post();
            if(notificationDTO.getPostDTO() != null){
                post = postService.findById(notificationDTO.getPostDTO().getId());
            }
            Notification notification = new Notification(0, notificationDTO.getMessage(), user, post);
            List<Sns> snsList = snsService.getByUserId(idUser);
            if(snsList != null) {
                if (snsList.get(0).getUser().getClass() != Offeror.class) {
                    for (Sns sns : snsList) {
                        //Costruisco il DTO
                        PushDTO pushDTO = new PushDTO("My LinkedIn", notification.getMessage(), sns.getArn());
                        //Invio richiesta ad API GW (=> invio notifica al client)
                        RestTemplate restTemplate = new RestTemplate();

                        HttpEntity<PushDTO> request1 = new HttpEntity<>(pushDTO);
                        restTemplate.postForObject("https://wg2bo6r10i.execute-api.us-east-1.amazonaws.com/sns/notifications/", request1, String.class);
                    }
                }
                else {
                    //Gestione Bulk notification per offeror
                    Thread t = new Thread(new Runner(snsList, notification));
                    t.start();
                }
            }
                return new NotificationDTO().dtoFromDomain(notificationService.saveNotification(notification));
        }
        else {throw new UserNotAuthorizedException();}
    }


    @GetMapping(value = "getSkilById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SkilDTO getSkilById(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws SkilNotFoundException, UserNotAuthorizedException, UserNotFoundException{
        if(isRegistered(request)!= 0){
            return new SkilDTO().dtoFromDomain(skilService.findById(id));
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    @PostMapping(value="/saveFirebaseToken", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveFireToken(@RequestBody FirebaseTokenDTO firebaseTokenDTO, HttpServletRequest request1, HttpServletResponse response1) throws UserNotFoundException {
        //To do
        // mandare token a sns
        System.out.println("\n\nSAVEFIREBASETOKEN API\n\n");

        RestTemplate restTemplate = new RestTemplate();
        //creo Json
        String jsonString = "{\"token\":\""+firebaseTokenDTO.getFirebaseToken()+"\"}";
        HttpEntity<String> request = new HttpEntity<>(jsonString);
        //attendere risposta da API GW
        SnsDTO snsDTO = restTemplate.postForObject("https://wg2bo6r10i.execute-api.us-east-1.amazonaws.com/sns/token/", request, SnsDTO.class);
        if(snsService.findByArn(snsDTO.getArn()) == null) {
            System.out.println("SNSARN: " + snsDTO.getArn());
            User user = userService.findById(isRegistered(request1));
            Sns sns = new Sns();
            sns.setUser(user);
            sns.setArn(snsDTO.getArn());
            //salvare in db endpointARN e relativo userId.
            snsService.saveARN(sns);
        }
        else
            System.out.println("ARN già esistente");
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
