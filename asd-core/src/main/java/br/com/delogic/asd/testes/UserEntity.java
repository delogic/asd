package br.com.delogic.asd.testes;

import java.util.Date;
import java.util.List;

public class UserEntity {

    public static final MetaData META = new MetaData(UserEntity.class)
        .add("id", new MetaField().required())
        .add("name", new MetaField().required().max(200))
        .add("email", new MetaField().required().type("email"))
        .add("password", new MetaField().required().max(200))
        .add("picture", new MetaField().max(100))
        .add("status", new MetaField().required().max(50))
        .add("roles", new MetaField().required())
//        .add("roles", new MetaField().required().min(1).each().required().max(100))
        .add("birthDate", new MetaField().min(new Date()).max(new Date()))
        .add("age", new MetaField().required().min(18).max(65));

    private Integer id;
    private String name;
    private String email;
    private String password;
    private String picture;
    private Status status;
    private List<String> roles;
    private Date birthDate;
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
