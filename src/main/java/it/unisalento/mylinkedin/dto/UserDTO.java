package it.unisalento.mylinkedin.dto;

import it.unisalento.mylinkedin.domain.User;
import it.unisalento.mylinkedin.validators.MatchFieldConstraint;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@MatchFieldConstraint(email = "email", emailMatch = "emailToVerify", password = "password", passwordMatch = "passwordToVerify")
public class UserDTO{

    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String birthday;
    @NotNull
    @Min(value = 18)
    int age;
    @NotBlank
    private String role;
    @NotBlank
    private String email;
    @NotBlank
    private String emailToVerify;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordToVerify;
    private String profileImagePath;

    public UserDTO(){}

    public UserDTO(int id, String name, String surname, String birthday, int age, String role, String email, String emailToVerify, String password, String passwordToVerify, String profileImagePath) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.age = age;
        this.role = role;
        this.email = email;
        this.emailToVerify = emailToVerify;
        this.password = password;
        this.passwordToVerify = passwordToVerify;
        this.profileImagePath = profileImagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailToVerify() {
        return emailToVerify;
    }

    public void setEmailToVerify(String emailToVerify) {
        this.emailToVerify = emailToVerify;
    }

    public String getPasswordToVerify() {
        return passwordToVerify;
    }

    public void setPasswordToVerify(String passwordToVerify) {
        this.passwordToVerify = passwordToVerify;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public UserDTO getUserDTOFromDomain(User user){
        String profileImagePath = null;
        if(user.getProfileImage() != null){
            profileImagePath = user.getProfileImage().getProfilePicturePath();
        }

        return new UserDTO(user.getId(), user.getName(), user.getSurname(), user.getBirthday(),
                            user.getAge(), "not registered", user.getEmail(), null,
                            user.getPassword(), null, profileImagePath);
    }
}
