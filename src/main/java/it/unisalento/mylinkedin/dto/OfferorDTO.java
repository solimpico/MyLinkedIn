package it.unisalento.mylinkedin.dto;

import it.unisalento.mylinkedin.domain.Offeror;

public class OfferorDTO extends UserDTO{
    boolean registerd;
    boolean enabling;
    CompanyDTO companyDTO;

    public OfferorDTO(){}

    public OfferorDTO(int id, String name, String surname, String birthday, int age, String role, String email, String emailToVerify, String password, String passwordToVerify, String profileImagePath, boolean registerd, boolean enabling, CompanyDTO companyDTO) {
        super(id, name, surname, birthday, age, role, email, emailToVerify, password, passwordToVerify, profileImagePath);
        this.registerd = registerd;
        this.enabling = enabling;
        this.companyDTO = companyDTO;
    }

    public boolean isRegisterd() {
        return registerd;
    }

    public void setRegisterd(boolean registerd) {
        this.registerd = registerd;
    }

    public boolean isEnabling() {
        return enabling;
    }

    public void setEnabling(boolean enabling) {
        this.enabling = enabling;
    }

    public CompanyDTO getCompanyDTO() {
        return companyDTO;
    }

    public void setCompanyDTO(CompanyDTO companyDTO) {
        this.companyDTO = companyDTO;
    }

    public OfferorDTO dtoFromDomain(Offeror offeror){
        String profileImagePath = null;
        if(offeror.getProfileImage() != null){
            profileImagePath = offeror.getProfileImage().getProfilePicturePath();
        }
        CompanyDTO companyDTO = new CompanyDTO();
        if(offeror.getCompany() != null){
            companyDTO = companyDTO.dtoFromDomain(offeror.getCompany());
        }
        else {
             companyDTO = null;
        }
        return new OfferorDTO(offeror.getId(), offeror.getName(), offeror.getSurname(), offeror.getBirthday(), offeror.getAge(), "Offeror", offeror.getEmail(), null, offeror.getPassword(), null, profileImagePath, offeror.isRegistered(), offeror.isEnabling(), companyDTO);
    }

}
