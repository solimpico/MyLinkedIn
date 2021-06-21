package it.unisalento.mylinkedin.restcontrollers;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import it.unisalento.mylinkedin.domain.*;
import it.unisalento.mylinkedin.dto.ApplicantDTO;
import it.unisalento.mylinkedin.dto.PdfDTO;
import it.unisalento.mylinkedin.dto.PostDTO;
import it.unisalento.mylinkedin.exceptions.PostNotFoundException;
import it.unisalento.mylinkedin.exceptions.SkilNotFoundException;
import it.unisalento.mylinkedin.exceptions.UserNotAuthorizedException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IApplicantService;
import it.unisalento.mylinkedin.iservices.IPostService;
import it.unisalento.mylinkedin.iservices.ISkilService;
import it.unisalento.mylinkedin.iservices.IUserService;
import it.unisalento.mylinkedin.security.JwtProvider;
import it.unisalento.mylinkedin.utility.URLFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/applicant")
public class ApplicantRestController {

    @Autowired
    IPostService postService;
    @Autowired
    IApplicantService applicantService;
    @Autowired
    IUserService userService;
    @Autowired
    ISkilService skilService;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping(value = "/saveOnPdf", consumes = MediaType.APPLICATION_JSON_VALUE)
    public URLFileDTO savePostsOnPdf(@RequestBody  PdfDTO pdfDTO, HttpServletRequest request, HttpServletResponse response) throws PostNotFoundException, UserNotAuthorizedException, UserNotFoundException {
        if(isApplicant(request) != 0){
            int[] postIds = pdfDTO.getIdPostArray();
            List<Post> postList = new ArrayList<>();
            for(int i = 0; i<postIds.length; i++){
                postList.add(postService.findById(postIds[i]));
            }
            String cwd = System.getProperty("user.dir");
            String nameOfPdf = new Date().getTime() +".pdf";
            String filename = cwd.substring(0,cwd.length()-10) + "build/libs/uploads/" + nameOfPdf;
            URLFileDTO urlFileDTO = new URLFileDTO("/public/download/"+nameOfPdf);
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filename));
                document.open();
                for(Post post : postList) {
                    String doc = post.getPostType().getType() + "\nAutore: " + post.getUser().getName()+ "\nData: "+post.getPublicationDate();
                    for (Data data : post.getDataList()) {
                        doc = doc + "\n" + data.getField() + ": " + data.getData();
                    }
                    doc = doc + "\n\n";
                    document.add(new Paragraph(doc));
                }
                document.close();

            } catch (DocumentException | IOException e) {
                System.out.println(e.getMessage());
            }
            return urlFileDTO;
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value = "/showVisibleByOfferor", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDTO> showVisibleByOfferor(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException{
        if(isApplicant(request) != 0){
            List<PostDTO> postDTOList = new ArrayList<>();
            List<Post> postList = postService.findVisibleByOfferor();
            for (Post post : postList) {
                PostDTO postDTO = new PostDTO();
                postDTOList.add(postDTO.dtoFromDomain(post));
            }
            return postDTOList;
        }
        else{
            throw new UserNotAuthorizedException();
        }
    }

    @GetMapping(value = "/showVisibleBySkil", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDTO> showVisibleBySkil(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, UserNotAuthorizedException {
        int userId = isApplicant(request);
        if(userId != 0){
            List<SkilApplicant> skilApplicantList = applicantService.findByUserId(userId).getSkilApplicantList();
            List<PostDTO> postDTOList = new ArrayList<>();
            for(SkilApplicant skilApplicant : skilApplicantList) {
                List<Post> postList = postService.findVisibleBySkil(skilApplicant.getSkil().getId());
                for (Post post : postList) {
                    PostDTO postDTO = new PostDTO();
                    postDTOList.add(postDTO.dtoFromDomain(post));
                }
            }
            return postDTOList;
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @PostMapping(value = "/addSkilToApplicant", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApplicantDTO addSkilToApplicant(@RequestBody ApplicantDTO applicantDTO, HttpServletRequest request, HttpServletResponse response) throws SkilNotFoundException, UserNotFoundException, UserNotAuthorizedException {
        if(isApplicant(request) != 0){
            Applicant applicant = applicantService.findByUserId(applicantDTO.getId());
            List<Skil> skilOfApplicant = skilService.findSkilByUserId(applicant.getId());
            if(skilOfApplicant == null) {
                for (int i : applicantDTO.getSkilIdArray()) {
                    SkilApplicant skilApplicant = new SkilApplicant(0, skilService.findById(i), applicant);
                    skilService.saveAssociation(skilApplicant);
                }
            }
            else {
                boolean exist;
                for (int i : applicantDTO.getSkilIdArray()) {
                    exist = false;
                    for (Skil skil : skilOfApplicant) {
                        if(skil.getId() == i) {
                            exist = true;
                        }
                    }
                    if(exist == false){
                        SkilApplicant skilApplicant = new SkilApplicant(0, skilService.findById(i), applicant);
                        skilService.saveAssociation(skilApplicant);
                    }
                }
            }
            return new ApplicantDTO().dtoFromDomain(applicant);
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    //CONTROL
    private int isApplicant(HttpServletRequest request) throws UserNotFoundException{
        String token = request.getHeader("Authorization").replace("Bearer ","");
        String email  = jwtProvider.decodeJwt(token).getSubject();
        User user = userService.isRegistered(email);
        if(user.getClass().equals(Applicant.class)){
            return user.getId();
        }
        return 0;
    }

}
