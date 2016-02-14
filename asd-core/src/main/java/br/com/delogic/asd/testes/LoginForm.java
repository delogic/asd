package br.com.delogic.asd.testes;

public class LoginForm {

    public static final MetaData META = new MetaData(LoginForm.class)
        .add("userEmail", UserEntity.META.extend("email"))
        .add("userPassword", UserEntity.META.extend("password"));

    private String userEmail;
    private String userPassword;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
