package br.com.delogic.asd.testes;

import java.util.Date;

public class UserProfileForm extends ChangePasswordForm {

    public static final MetaData META = ChangePasswordForm.META.extend()
        .add("name", UserEntity.META.extend("name"))
        .add("picture", UserEntity.META.extend("picture"))
        .add("birthDate", UserEntity.META.extend("birthDate"));

    private String name;
    private String picture;
    private Date birthDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

}
