package br.com.delogic.asd.testes;

import java.util.Date;

public class MetaTeste {

    public static void main(String[] args) {

        MetaData userMetaData = new MetaData();

        userMetaData.put("id", new MetaField().id());
        userMetaData.put("name", new MetaField().max(100));
        userMetaData.put("email", new MetaField().required().max(200).format("mailregex"));
        userMetaData.put("password", new MetaField().max(100));
        userMetaData.put("picture", new MetaField().max(100));
        userMetaData.put("status", new MetaField().required().max(50));
        userMetaData.put("roles", new MetaField().atLeast(1).each().required().max(100));
        userMetaData.put("birthDate", new MetaField().required().between(new Date(), new Date()));
        userMetaData.put("age", new MetaField().required().greaterThan(18).lessThan(65));

        MetaData login = new MetaData();
        login.put("userEmail", userMetaData.extend("email"));
        login.put("password", userMetaData.extend("password"));

        MetaData changePassword = new MetaData();
        changePassword.put("password1", userMetaData.extend("password"));
        changePassword.put("password2", userMetaData.extend("password"));
        changePassword.put("*", new MetaField().check(new Constraint<ChangePasswordModel>() {
            @Override
            public boolean check(ChangePasswordModel model) {
                return model.password1.equals(model.password2);
            }

        }));

        MetaData profile = new MetaData();
        profile.put("name", userMetaData.extend("name"));
        profile.put("email", userMetaData.extend("email"));
        profile.put("picture", userMetaData.extend("picture"));
        profile.put("roles", userMetaData.extend("roles"));
        profile.put("birthDay", userMetaData.extend("birthDate")); //fields are different on purpose
        

    }

}
