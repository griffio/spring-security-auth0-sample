package com.auth0.spring.security.auth0;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Base64;

public class Auth0AuthenticationProvider implements AuthenticationProvider, InitializingBean {

  private final String clientId;
  private final String clientSecret;
  private final String issuer;

  public Auth0AuthenticationProvider(String clientId, String clientSecret, String issuer) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.issuer = issuer;
  }

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    if (!(authentication instanceof Auth0BearerAuthentication)) {
      return null;
    }

    try {
      String bearerToken = authentication.getPrincipal().toString();
      byte[] signingKey = Base64.getUrlDecoder().decode(clientSecret);
      Jws<Claims> jws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(bearerToken);
      Claims claims = jws.getBody();
      return Auth0JWTAuthentication.create(claims);
    } catch (Exception e) {
      throw new Auth0TokenException(e);
    }

  }

  public boolean supports(Class<?> authentication) {
    return Auth0BearerAuthentication.class.isAssignableFrom(authentication);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
  }

  public String getClientId() {
    return clientId;
  }

  public String getIssuer() {
    return issuer;
  }

  public String getClientSecret() {
    return clientSecret;
  }
}