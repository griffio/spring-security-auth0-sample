package com.auth0.spring.security.auth0;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Map;

import com.auth0.jwt.JWTVerifyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.auth0.jwt.JWTVerifier;

/**
 * Class that verifies the JWT token and in case of beeing valid, it will set
 * the userdetails in the authentication object
 *
 * @author Daniel Teixeira
 */
public class Auth0AuthenticationProvider implements AuthenticationProvider, InitializingBean {

  private JWTVerifier jwtVerifier = null;
  private String clientSecret = null;
  private String clientId = null;
  private final Logger logger = LoggerFactory.getLogger(Auth0AuthenticationProvider.class);
  private static final AuthenticationException AUTH_ERROR = new Auth0TokenException("Authentication error occured");

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String token = ((Auth0JWTToken) authentication).getJwt();

    logger.info("Trying to authenticate with token: " + token);

    Map<String, Object> decoded;
    try {

      Auth0JWTToken tokenAuth = ((Auth0JWTToken) authentication);
      decoded = jwtVerifier.verify(token);
      logger.debug("Decoded JWT token" + decoded);
      tokenAuth.setAuthenticated(true);
      tokenAuth.setPrincipal(new Auth0UserDetails(decoded));
      tokenAuth.setDetails(decoded);
      return authentication;

    } catch (SignatureException | InvalidKeyException | NoSuchAlgorithmException | JWTVerifyException
        | IllegalStateException | IOException e) {
      logger.info("InvalidKeyException thrown while decoding JWT token " + e.getLocalizedMessage());
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
    jwtVerifier = new JWTVerifier(Base64.getUrlDecoder().decode(clientSecret), clientId);
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
