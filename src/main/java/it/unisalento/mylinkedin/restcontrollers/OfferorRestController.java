package it.unisalento.mylinkedin.restcontrollers;

import it.unisalento.mylinkedin.domain.Administrator;
import it.unisalento.mylinkedin.domain.Company;
import it.unisalento.mylinkedin.domain.Offeror;
import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.dto.CompanyDTO;
import it.unisalento.mylinkedin.exceptions.CompanyException;
import it.unisalento.mylinkedin.exceptions.UserNotAuthorizedException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IOfferorService;
import it.unisalento.mylinkedin.iservices.IUserService;
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
@RequestMapping(value = "/offeror")
public class OfferorRestController {
    @Autowired
    IOfferorService offerorService;
    @Autowired
    IUserService userService;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping(value = "/addCompany", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO addCompany(@RequestBody  @Valid CompanyDTO companyDTO, HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException, CompanyException, UserNotAuthorizedException {
        if(isOfferor(request)){
            Company comp = offerorService.findCompanyByName(companyDTO.getName());
            List<Offeror> offerorList = new ArrayList<>();
            Offeror offeror = offerorService.findByUserId(companyDTO.getIdOfferor()[0]);
            if(comp == null) {
                //Company non esiste
                offerorList.add(offeror);
                Company company = new Company(companyDTO.getId(), companyDTO.getName(), companyDTO.getAddress(), companyDTO.getDescription(), offerorList);
                offerorService.addCompany(offeror, company);
                return new CompanyDTO().dtoFromDomain(company);
            }
            else {
                //Company esiste
                offerorList = comp.getOfferorList();
                offerorList.add(offeror);
                comp.setOfferorList(offerorList);
                offerorService.addCompany(offeror, comp);
                return new CompanyDTO().dtoFromDomain(comp);

            }
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    @DeleteMapping(value = "/deleteCompany/{idCompany}")
    public ResponseEntity<Company> deleteCompany(@PathVariable int idCompany, HttpServletRequest request, HttpServletResponse response) throws CompanyException, UserNotAuthorizedException, UserNotFoundException{
        if(isOfferor(request)){
            offerorService.deleteCompany(idCompany);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            throw new UserNotAuthorizedException();
        }
    }

    //CONTROL
    private boolean isOfferor(HttpServletRequest request) throws UserNotFoundException{
        String token = request.getHeader("Authorization").replace("Bearer ","");
        String email  = jwtProvider.decodeJwt(token).getSubject();
        User user = userService.isRegistered(email);
        if(user.getClass().equals(Offeror.class)){
            return true;
        }
        return false;
    }
}
