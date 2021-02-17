package br.com.delogic.asd.email;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import br.com.delogic.asd.oauth.AccessTokenRepository;

public class OAuthJavaMailSenderImpl extends JavaMailSenderImpl {

    private AccessTokenRepository accessTokenRepository;
    private final ApplicationContext ctx;

    public OAuthJavaMailSenderImpl(ApplicationContext ctx) throws Exception {
        this.ctx = ctx;
        this.init();
    }

    public String getPassword() {
        if (accessTokenRepository == null) {
            return super.getPassword();
        }
        return accessTokenRepository.getAccessToken();
    }

    public void init() throws Exception {
        Environment env = ctx.getEnvironment();
        if (env.getProperty("mail.smtp.auth.mechanisms", "").contains("OAUTH")) {
            String authenticatorType = env.getRequiredProperty("oauth.accesstoken.repository");
            AutowireCapableBeanFactory factory = ctx.getAutowireCapableBeanFactory();
            accessTokenRepository = (AccessTokenRepository) factory.createBean(Class.forName(authenticatorType));
        }
    };

}
