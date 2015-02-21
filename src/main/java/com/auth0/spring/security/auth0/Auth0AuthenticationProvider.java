package com.auth0.spring.security.auth0;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.text.ParseException;

/**
 * Class that verifies the JWT token and in case of beeing valid, it will set
 * the userdetails in the authentication object
 *
 * @author Daniel Teixeira
 */
public class Auth0AuthenticationProvider implements AuthenticationProvider, InitializingBean {

  private String clientSecret;
  private String clientId;
  private final Logger logger = LoggerFactory.getLogger(Auth0AuthenticationProvider.class);
  private static final AuthenticationException AUTH_ERROR = new Auth0TokenException("Authentication error occured");

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    Auth0JWTToken auth0JWTAuthentication = (Auth0JWTToken) authentication;

    JWT jwt = auth0JWTAuthentication.getJwt();

    logger.info(jwt.serialize());

    try {
      ReadOnlyJWTClaimsSet claimsSet = JWTParser.parse(jwt.serialize(), new Auth0JWTVerifyHandler(clientSecret));
      auth0JWTAuthentication.setAuthenticated(claimsSet != null);
      if (claimsSet != null) {
        auth0JWTAuthentication.setPrincipal(new Auth0UserDetails(claimsSet.getAllClaims()));
        auth0JWTAuthentication.setDetails(jwt);
      }
      return authentication;
    } catch (ParseException e) {
      throw AUTH_ERROR;
    }
  }

  public boolean supports(Class<?> authentication) {
    return Auth0JWTToken.class.isAssignableFrom(authentication);
  }

  public void afterPropertiesSet() throws Exception {
    if ((clientSecret == null) || (clientId == null)) {
      throw new RuntimeException("client secret and client id are not set for Auth0AuthenticationProvider");
    }
  }


  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

}
