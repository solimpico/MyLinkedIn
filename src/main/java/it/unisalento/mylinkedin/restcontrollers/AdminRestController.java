package it.unisalento.mylinkedin.restcontrollers;

import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.*;
import it.unisalento.mylinkedin.exceptions.*;
import it.unisalento.mylinkedin.iservices.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

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

    //GESTIONE UTENTI ---------------------------------------------------------

    @PutMapping(value = "/confirmRegApplicant/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO confirmRegistration(@PathVariable int id) throws UserNotFoundException{
        try{
            return new ApplicantDTO().dtoFromDomain(applicantService.confirmAndEnable(id));
        }
        catch (UserNotFoundException e){}
        try{
            return new OfferorDTO().dtoFromDomain(offerorService.confirmAndEnable(id));
        }
        catch (UserNotFoundException e){}
        return null;
    }

    @GetMapping(value = "/getRegistrationRequest", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getRegistrationRequest(){
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.addAll(new OfferorDTO().listDTOFromListDomain(offerorService.findOfferorRegistrationRequest()));
        userDTOList.addAll(new ApplicantDTO().listDTOFromListDomain(applicantService.findApplicantRegistrationRequest()));
        return userDTOList;
    }

    //GESTIONE TIPOLOGIE DI POST -----------------------------------------------------------

    @PostMapping(value = "/addType", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PostTypeDTO addType(@RequestBody @Valid PostTypeDTO postTypeDTO) throws AddPostTypeException {
        PostType postType = new PostType(0, postTypeDTO.getType(), null, null);
        PostType postTypeSaved = postTypeService.addPostType(postType, postTypeDTO.getRequiredFields());
        if(postTypeSaved == null){
            return null;
        }
        return new PostTypeDTO().dtoFromDomain(postTypeSaved);
    }

    @GetMapping(value = "/showExistingRequiredField", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RequiredFieldDTO> showRequiredField(){
        List<RequiredFieldDTO> requiredFieldDTOList = new ArrayList<>();
        List<RequiredField> requiredFieldList =  requiredFieldsService.getAll();
        for (RequiredField reqField : requiredFieldList) {
            RequiredFieldDTO requiredFieldDTO = new RequiredFieldDTO(reqField.getId(), reqField.getRequiredField());
            requiredFieldDTOList.add(requiredFieldDTO);
        }
        return requiredFieldDTOList;
    }

    @GetMapping(value = "/showExistingType", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostTypeDTO> showType(){

        List<PostTypeDTO> postTypeDTOList = new ArrayList<>();
        List<PostType> postTypeList =  postTypeService.showAllPostType();
        for (PostType type : postTypeList){
            postTypeDTOList.add(new PostTypeDTO().dtoFromDomain(type));
        }
        return postTypeDTOList;
    }

    @PostMapping(value = "/addSkil", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SkilDTO addSkil(@RequestBody @Valid SkilDTO skilDTO) throws SkilException{
        return new SkilDTO().dtoFromDomain(skilService.saveSkil(new Skil(0, skilDTO.getSkilName(), null, null)));
    }

    @DeleteMapping(value = "/deleteSkil/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Skil> deleteSkil(@PathVariable int id) throws SkilNotFoundException {
        skilService.deleteSkil(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/showAllSkils", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SkilDTO> showAllSkil(){
        List<Skil> skilList = skilService.findAll();
        List<SkilDTO> skilDTOList = new ArrayList<>();
        for (Skil skil : skilList){
            skilDTOList.add(new SkilDTO().dtoFromDomain(skil));
        }
        return skilDTOList;
    }

    @DeleteMapping(value = "/deletePostType/{idPostType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostType> deletePostType(@PathVariable int idPostType) throws PostException{
        postTypeService.deletePostTypeById(idPostType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //GESTION POST ------------------------------------------------------------------------------
    @PutMapping(value = "/hidenPost/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO hidenPost(@PathVariable int id) throws PostNotFoundException {
        Post postVisible = postService.findById(id);
        postVisible.setVisible(false);
        PostDTO postDTO = new PostDTO();
        return postDTO.dtoFromDomain(postVisible);
    }

    @PutMapping(value = "/setPostVisible/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDTO makeVisiblePost(@PathVariable int id) throws PostNotFoundException{
        Post postVisible = postService.findById(id);
        postVisible.setVisible(true);
        PostDTO postDTO = new PostDTO();
        return postDTO.dtoFromDomain(postVisible);
    }

    @GetMapping(value = "/showAllPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDTO> showAll(){
        List<PostDTO> postDTOList = new ArrayList<>();
        List<Post> postList = postService.findAll();
        for (Post post : postList) {
            PostDTO postDTO = new PostDTO();
            postDTOList.add(postDTO.dtoFromDomain(post));
        }
        return postDTOList;
    }

}
