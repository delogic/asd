package br.com.delogic.asd.email;

import java.util.Map.Entry;
import java.util.Properties;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import br.com.delogic.asd.config.SpringTestBase;

@Ignore("Test for local debugging")
public class OAuthJavaMailSenderTest extends SpringTestBase {

    /**
     * Fill the e-mail subject when debugging locally
     */

    @Inject
    private ApplicationContext ctx;

    @Value("test.properties")
    private Resource propertiesResource;

    @Inject
    private Environment env;

    @Test
    public void shouldSendByJavaMailSender() throws Exception {
        String emailFrom = env.getRequiredProperty("mail.smtp.user");

        OAuthJavaMailSenderImpl sender = new OAuthJavaMailSenderImpl(ctx);

        Properties allProperties = new Properties();
        allProperties.load(propertiesResource.getInputStream());
        Properties mailProperties = new Properties();

        for (Entry<Object, Object> prop : allProperties.entrySet()) {
            if (prop.getKey().toString().startsWith("mail.")) {
                mailProperties.put(prop.getKey(), prop.getValue());
            }
        }

        sender.setDefaultEncoding("UTF-8");
        sender.setJavaMailProperties(mailProperties);

        EmailManager mailer = new EmailManagerImpl(sender);
        mailer.send(
            new EmailAddress("Person", emailFrom),
            new EmailContent("Subject", "content"),
            new EmailAddress("Name", emailFrom));

        mailer.send(
            new EmailAddress("Person", emailFrom),
            new EmailContent("Subject", "content"),
            new EmailAddress("Name", emailFrom));
    }

    // @Test
    // public void shouldSendByTransport() throws Exception {
    // final AccessTokenRepository repository = new GoogleAccessTokenRepository(
    // new ObjectMapper(),
    // new
    // ClassPathResource("oauthdelogic1-3664cf495af6.json").getInputStream(),
    // emailFrom);
    //
    // Properties props = new Properties();
    // props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
    // props.put("mail.transport.protocol", "smtps");
    // props.put("mail.smtp.host", "smtp.gmail.com");
    // props.put("mail.smtp.port", 587);
    // props.put("mail.smtp.starttls.enable", "true");
    // props.put("mail.smtp.starttls.required", "true");
    // props.put("mail.smtp.auth", "true");
    // props.put("mail.debug", "true");
    // props.put("mail.debug.auth", "true");
    //
    // Session session = Session.getInstance(props, new Authenticator() {
    // @Override
    // protected PasswordAuthentication getPasswordAuthentication() {
    // return new PasswordAuthentication(emailFrom,
    // repository.getAccessToken());
    // }
    // });
    //
    // Message message = new MimeMessage(session);
    // // e-mail precisa ser um e-mail verificado
    // message.setFrom(new InternetAddress(emailFrom));
    // message.setRecipients(
    // Message.RecipientType.TO, InternetAddress.parse(emailFrom));
    // message.setSubject("Gmail Test");
    //
    // String msg = "E-mail test from a Google Service Account. Received me?
    // </br>";
    //
    // MimeBodyPart mimeBodyPart = new MimeBodyPart();
    // mimeBodyPart.setContent(msg, "text/html");
    //
    // Multipart multipart = new MimeMultipart();
    // multipart.addBodyPart(mimeBodyPart);
    //
    // message.setContent(multipart);
    //
    // try {
    // Transport.send(message);
    // } catch (Exception e) {
    // System.err.println(e);
    // throw e;
    // }
    //
    // }

}
