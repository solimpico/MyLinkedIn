package it.unisalento.mylinkedin.restcontrollers;

import it.unisalento.mylinkedin.dao.UserRepository;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.*;
import it.unisalento.mylinkedin.exceptions.MessageException;
import it.unisalento.mylinkedin.exceptions.SavingUserException;
import it.unisalento.mylinkedin.exceptions.SkilNotFoundException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.*;
import it.unisalento.mylinkedin.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/public")
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
    private JwtProvider jwtProvider;


    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginTokenDTO> authenticate(@RequestBody LoginInputDTO body) throws UserNotFoundException{
        User user = userService.findByEmail(body.getEmail());
        LoginTokenDTO tokenDTO = new LoginTokenDTO();
        if(user != null && user.getEmail().equals(body.getEmail()) && user.getPassword().equals(body.getPassword())) {
            String jwt = jwtProvider.createJwt();
            tokenDTO.setToken(jwt);
            return ResponseEntity.ok(tokenDTO);
        }
        else return ResponseEntity.ok(tokenDTO);
    }


    @PostMapping(value = "/registrationRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "/sendMessage", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public MessageDTO sendMessage(@RequestBody @Valid MessageDTO messageDTO) throws UserNotFoundException, MessageException {
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

    @GetMapping(value = "showConversation/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageDTO> showConversation(@PathVariable int userId) throws MessageException{
        List<Message> messageList = messageService.getMessageByUserId(userId);
        List<MessageDTO> messageDTOList = new ArrayList<>();
        for (Message message : messageList){
            messageDTOList.add(new MessageDTO().dtoFromDomain(message));
        }
        return messageDTOList;
    }

    @PutMapping(value = "/enablingUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO enablingUser(@PathVariable int id) throws UserNotFoundException {
        if(applicantService.findByUserId(id) != null){
            Applicant applicant = applicantService.findByUserId(id);
            applicant.setEnabling(true);
            return new ApplicantDTO().dtoFromDomain(applicantService.save(applicant));
        }
        else if (offerorService.findByUserId(id) != null){
            Offeror offeror = offerorService.findByUserId(id);
            offeror.setEnabling(true);
            return new OfferorDTO().dtoFromDomain(offerorService.save(offeror));
        }
        return null;
    }

    @PutMapping(value = "/disablingUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO disablingUser(@PathVariable int id) throws UserNotFoundException{
        if(applicantService.findByUserId(id) != null){
            Applicant applicant = applicantService.findByUserId(id);
            applicant.setEnabling(false);
            applicantService.save(applicant);
            return new ApplicantDTO().dtoFromDomain(applicant);
        }
        else if (offerorService.findByUserId(id) != null){
            Offeror offeror = offerorService.findByUserId(id);
            offeror.setEnabling(false);
            offerorService.save(offeror);
            return new OfferorDTO().dtoFromDomain(offeror);
        }
        return null;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deleteUser(@PathVariable int id) throws UserNotFoundException{
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/getById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO showUser(@PathVariable int id) throws UserNotFoundException{
        if(applicantService.findByUserId(id) != null){
            return new ApplicantDTO().dtoFromDomain(applicantService.findByUserId(id));
        }
        else if (offerorService.findByUserId(id) != null){
            return new OfferorDTO().dtoFromDomain(offerorService.findByUserId(id));
        }
        return null;
    }

    @GetMapping(value = "/prova", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO prova(){
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Pippo");
        userDTO.setSurname("Male");
        return userDTO;
    }


    }
