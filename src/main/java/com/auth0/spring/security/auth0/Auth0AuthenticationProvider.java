package com.auth0.spring.security.auth0;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.text.ParseException;

public class Auth0AuthenticationProvider implements AuthenticationProvider, InitializingBean {

  private String clientSecret;
  private String clientId;
  private final Logger logger = LoggerFactory.getLogger(Auth0AuthenticationProvider.class);
  private MACVerifier macClientSecret;

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    if (!(authentication instanceof Auth0BearerAuthentication)) {
      return null;
    }

    final ReadOnlyJWTClaimsSet claimsSet;
    final SignedJWT jwt;

    try {
      String idToken = authentication.getPrincipal().toString();
      logger.info(idToken);
      jwt = SignedJWT.parse(idToken);
      claimsSet = jwt.getJWTClaimsSet();
    } catch (ParseException e) {
      throw new Auth0TokenException(e);
    }

    try {
      if (!jwt.verify(macClientSecret)) {
        throw new Auth0TokenException("Failed to Verify jwt");
      }
    } catch (JOSEException e) {
      throw new Auth0TokenException(e);
    }

    return Auth0JWTAuthentication.create(claimsSet);

  }

  public boolean supports(Class<?> authentication) {
    return Auth0BearerAuthentication.class.isAssignableFrom(authentication);
  }

  public void afterPropertiesSet() throws Exception {
    if ((clientSecret == null) || (clientId == null)) {
      throw new RuntimeException("client secret and client id are not set for Auth0AuthenticationProvider");
    }

    macClientSecret = new MACVerifier(clientSecret);
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
