package it.unisalento.mylinkedin.dto;

import it.unisalento.mylinkedin.domain.Applicant;
import it.unisalento.mylinkedin.domain.SkilApplicant;

public class ApplicantDTO extends UserDTO{
    boolean registered;
    boolean enabling;
    private int[] skilIdArray;

    public ApplicantDTO() {}

    public ApplicantDTO(int id, String name, String surname, String birthday, int age, String role, String email, String emailToVerify, String password, String passwordToVerify, String profileImagePath, boolean registered, boolean enabling, int[] skilIdArray) {
        super(id, name, surname, birthday, age, role, email, emailToVerify, password, passwordToVerify, profileImagePath);
        this.registered = registered;
        this.enabling = enabling;
        this.skilIdArray = skilIdArray;
    }

    public ApplicantDTO dtoFromDomain(Applicant applicant){
        String profileImagePath = null;
        if(applicant.getProfileImage() != null){
            profileImagePath = applicant.getProfileImage().getProfilePicturePath();
        }
        int[] skilIdArray = new int[applicant.getSkilApplicantList().size()];
        int i = 0;
        for(SkilApplicant skilApplicant : applicant.getSkilApplicantList()){
            skilIdArray[i] = skilApplicant.getSkil().getId();
            i++;
        }
        return new ApplicantDTO(applicant.getId(), applicant.getName(), applicant.getSurname(), applicant.getBirthday(), applicant.getAge(), "Applicant", applicant.getEmail(), null, applicant.getPassword(), null, profileImagePath, applicant.isRegistered(), applicant.isEnabling(), skilIdArray);
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean isEnabling() {
        return enabling;
    }

    public void setEnabling(boolean enabling) {
        this.enabling = enabling;
    }

    public int[] getSkilIdArray() {
        return skilIdArray;
    }

    public void setSkilIdArray(int[] skilIdArray) {
        this.skilIdArray = skilIdArray;
    }
}
