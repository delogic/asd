package br.com.delogic.asd.oauth;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

/**
 * Repository to return the Access Token for the underlying Cloud service.
 * 
 * @author celio@delogic.com.br
 *
 */
public interface AccessTokenRepository extends InitializingBean, ApplicationContextAware {

    /**
     * Returns the accessToken required to connect to the underlying service. Do
     * not store this value as it might be required to refresh, always ask for a
     * new one when necessary.
     * 
     * @return accessToken to use the underlying service
     */
    public String getAccessToken();

}
