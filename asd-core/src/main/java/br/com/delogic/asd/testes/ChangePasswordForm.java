package br.com.delogic.asd.testes;

public class ChangePasswordForm {

    public static final MetaData META = new MetaData(ChangePasswordForm.class)
        .add("password1", UserEntity.META.extend("password"))
        .add("password2", UserEntity.META.extend("password"))
        .constraint(new Constraint<ChangePasswordForm>() {
            @Override
            public boolean check(ChangePasswordForm e) {
                return e.password1.equals(e.password2);
            }
        });

    private String password1;
    private String password2;

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

}
