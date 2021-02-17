package br.com.delogic.asd.oauth;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleAccessTokenRepository implements AccessTokenRepository {

    private Algorithm algorithm;
    private Map<String, String> params;
    private ObjectMapper mapper;
    private String accessToken;
    private long accessTokenExpiresTimeMilis;
    private final Object lock = new byte[0];
    private String emailImpersonated;
    private ApplicationContext ctx;
    private Environment env;
    private Integer expiresAt;

    protected void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // ignore this exception as resource might already be closed
            }
        }
    }

    @Override
    public String getAccessToken() {
        synchronized (lock) {
            if (shouldRefreshAccessToken()) {
                refreshAccessToken();
            }
            return accessToken;
        }
    }

    protected boolean shouldRefreshAccessToken() {
        return accessToken == null || (System.currentTimeMillis() + 600.000) > accessTokenExpiresTimeMilis;
    }

    @SuppressWarnings("unchecked")
    protected void refreshAccessToken() {
        long now = System.currentTimeMillis();

        Builder jwtBuilder = JWT.create()
            .withIssuer(params.get("client_email"))
            .withClaim("scope", "https://mail.google.com/")
            .withAudience(params.get("token_uri"))
            .withSubject(emailImpersonated)
            .withIssuedAt(new Date(now))
            .withExpiresAt(new Date(now + (expiresAt * 1000)))
            .withKeyId(params.get("private_key_id"));

        String assertion = jwtBuilder.sign(algorithm);
        try {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
            requestParams.put("assertion", assertion);

            String content = mapper.writeValueAsString(requestParams);

            RestTemplate template = new RestTemplate();
            ResponseEntity<String> response = template.postForEntity(params.get("token_uri"), content, String.class);

            Map<String, Object> value = mapper.readValue(response.getBody(), Map.class);

            accessToken = (String) value.get("access_token");
            accessTokenExpiresTimeMilis = System.currentTimeMillis() + (1000 * (int) value.get("expires_in")); // returns
                                                                                                               // in
                                                                                                               // seconds
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {
        this.emailImpersonated = env.getRequiredProperty("mail.smtp.user");
        this.expiresAt = env.getProperty("oauth.accesstoken.expiresat", Integer.class, 3600);
        this.mapper = new ObjectMapper();

        InputStream jsonFile = new ClassPathResource(env.getRequiredProperty("oauth.accesstoken.file")).getInputStream();
        this.params = mapper.readValue(jsonFile, Map.class);
        close(jsonFile);

        PemReader reader = new PemReader(new StringReader(params.get("private_key")));
        PemObject pemObject = reader.readPemObject();
        close(reader);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
        KeyFactory factory = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) factory.generatePrivate(privateKeySpec);

        this.algorithm = Algorithm.RSA256(null, privateKey);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
        this.env = ctx.getEnvironment();
    }

}
