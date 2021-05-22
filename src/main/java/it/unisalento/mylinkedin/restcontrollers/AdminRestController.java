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
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private String tokenHeader;

    @Autowired
    IApplicantService applicantService;
    @Autowired
    IOfferorService offerorService;
    @Autowired
    IUserService userService;
    @Autowired
    IPostTypeService postTypeService;
    @Autowired
    IRequiredFieldsService requiredFieldsService;
    @Autowired
    IPostService postService;
    @Autowired
    ISkilService skilService;
    @Autowired
    JwtProvider jwtProvider;


    //GESTIONE UTENTI ---------------------------------------------------------

    @PutMapping(value = "/confirmReg/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO confirmRegistration(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{

        if(isAdministrator(request)) {
            try {
                return new ApplicantDTO().dtoFromDomain(applicantService.confirmAndEnable(id));
            } catch (UserNotFoundException e) {
            }
            try {
                return new OfferorDTO().dtoFromDomain(offerorService.confirmAndEnable(id));
            } catch (UserNotFoundException e) {
            }
        }
        else{
            throw new UserNotAuthorizedException();
        }
        return null;
    }

    @GetMapping(value = "/getRegistrationRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getRegistrationRequest(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isAdministrator(request)) {
            List<UserDTO> userDTOList = new ArrayList<>();
            userDTOList.addAll(new OfferorDTO().listDTOFromListDomain(offerorService.findOfferorRegistrationRequest()));
            userDTOList.addAll(new ApplicantDTO().listDTOFromListDomain(applicantService.findApplicantRegistrationRequest()));
            return userDTOList;
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    //GESTIONE TIPOLOGIE DI POST -----------------------------------------------------------

    @PostMapping(value = "/addType", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PostTypeDTO addType(@RequestBody @Valid PostTypeDTO postTypeDTO, HttpServletRequest request, HttpServletResponse response) throws AddPostTypeException, UserNotAuthorizedException, UserNotFoundException {
        if(isAdministrator(request)){
            PostType postType = new PostType(0, postTypeDTO.getType(), null, null);
            PostType postTypeSaved = postTypeService.addPostType(postType, postTypeDTO.getRequiredFields());
            if(postTypeSaved == null){
                return null;
            }
            return new PostTypeDTO().dtoFromDomain(postTypeSaved);
        }
        else {
            throw new UserNotAuthorizedException();
        }

    }

    @GetMapping(value = "/showExistingRequiredField", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RequiredFieldDTO> showRequiredField(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isAdministrator(request)){
            List<RequiredFieldDTO> requiredFieldDTOList = new ArrayList<>();
            List<RequiredField> requiredFieldList =  requiredFieldsService.getAll();
            for (RequiredField reqField : requiredFieldList) {
                RequiredFieldDTO requiredFieldDTO = new RequiredFieldDTO(reqField.getId(), reqField.getRequiredField());
                requiredFieldDTOList.add(requiredFieldDTO);
            }
            return requiredFieldDTOList;
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value = "/showExistingType", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostTypeDTO> showType(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isAdministrator(request)){
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

    @PostMapping(value = "/addSkil", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SkilDTO addSkil(@RequestBody @Valid SkilDTO skilDTO, HttpServletRequest request, HttpServletResponse response) throws SkilException, UserNotAuthorizedException, UserNotFoundException{
        if(isAdministrator(request)){
            return new SkilDTO().dtoFromDomain(skilService.saveSkil(new Skil(0, skilDTO.getSkilName(), null, null)));
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @DeleteMapping(value = "/deleteSkil/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Skil> deleteSkil(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws SkilNotFoundException, UserNotFoundException, UserNotAuthorizedException {
        if(isAdministrator(request)){
            skilService.deleteSkil(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value = "/showAllSkils", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SkilDTO> showAllSkil(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isAdministrator(request)){
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

    @DeleteMapping(value = "/deletePostType/{idPostType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostType> deletePostType(@PathVariable int idPostType, HttpServletRequest request, HttpServletResponse response) throws PostException, UserNotAuthorizedException, UserNotFoundException{
        if(isAdministrator(request)){
            postTypeService.deletePostTypeById(idPostType);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    //GESTION POST ------------------------------------------------------------------------------
    @PutMapping(value = "/hidenPost/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO hidenPost(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws PostNotFoundException, UserNotFoundException, UserNotAuthorizedException {
        if(isAdministrator(request)){
            Post postVisible = postService.findById(id);
            postVisible.setVisible(false);
            PostDTO postDTO = new PostDTO();
            return postDTO.dtoFromDomain(postVisible);
        }
        else{
            throw new UserNotAuthorizedException();
        }
    }

    @PutMapping(value = "/setPostVisible/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO makeVisiblePost(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws PostNotFoundException, UserNotFoundException, UserNotAuthorizedException{
        if(isAdministrator(request)){
            Post postVisible = postService.findById(id);
            postVisible.setVisible(true);
            PostDTO postDTO = new PostDTO();
            return postDTO.dtoFromDomain(postVisible);
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value = "/showAllPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDTO> showAll(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isAdministrator(request)){
            List<PostDTO> postDTOList = new ArrayList<>();
            List<Post> postList = postService.findAll();
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

    @PutMapping(value = "/enablingUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO enablingUser(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException {
        if(isAdministrator(request)){
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
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @PutMapping(value = "/disablingUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO disablingUser(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isAdministrator(request)){
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
        else {
            throw new UserNotAuthorizedException();
        }
    }

    //CONTROL
    private boolean isAdministrator(HttpServletRequest request) throws UserNotFoundException{
        String token = request.getHeader("Authorization").replace("Bearer ","");
        String email  = jwtProvider.decodeJwt(token).getSubject();
        User user = userService.isRegistered(email);
        if(user.getClass().equals(Administrator.class)){
            return true;
        }
        return false;
    }

}
