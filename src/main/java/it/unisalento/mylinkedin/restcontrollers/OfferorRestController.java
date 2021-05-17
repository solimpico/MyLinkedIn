package it.unisalento.mylinkedin.restcontrollers;

import it.unisalento.mylinkedin.domain.Company;
import it.unisalento.mylinkedin.domain.Offeror;
import it.unisalento.mylinkedin.dto.CompanyDTO;
import it.unisalento.mylinkedin.exceptions.CompanyException;
import it.unisalento.mylinkedin.exceptions.UserNotFoundException;
import it.unisalento.mylinkedin.iservices.IOfferorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/offeror")
public class OfferorRestController {
    @Autowired
    IOfferorService offerorService;

    @PostMapping(value = "/addCompany", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO addCompany(@RequestBody  @Valid CompanyDTO companyDTO) throws UserNotFoundException, CompanyException {
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

    @DeleteMapping(value = "/deleteCompany/{idCompany}")
    public ResponseEntity<Company> deleteCompany(@PathVariable int idCompany) throws CompanyException{
        offerorService.deleteCompany(idCompany);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
