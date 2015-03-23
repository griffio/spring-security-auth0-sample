package com.auth0.spring.security.auth0;

import com.google.common.base.Joiner;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.text.ParseException;
import java.time.Clock;
import java.util.Base64;
import java.util.Date;

public class Auth0AuthenticationProvider implements AuthenticationProvider, InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(Auth0AuthenticationProvider.class);

  private final String clientId;
  private final String issuer;
  private final Clock verifierClock;
  private final MACVerifier macClientSecret;

  public Auth0AuthenticationProvider(String clientId, String clientSecret, String issuer, Clock verifyClock) {
    this.clientId = clientId;
    this.issuer = issuer;
    this.verifierClock = verifyClock;
    this.macClientSecret = new MACVerifier(Base64.getUrlDecoder().decode(clientSecret));
  }

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    if (!(authentication instanceof Auth0BearerAuthentication)) {
      return null;
    }

    final ReadOnlyJWTClaimsSet claimsSet;
    final SignedJWT jwt;

    try {
      String idToken = authentication.getPrincipal().toString();
      jwt = SignedJWT.parse(idToken);
      claimsSet = jwt.getJWTClaimsSet();
      logger.info(Joiner.on(',').withKeyValueSeparator("=").join(claimsSet.getAllClaims()));
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

    Date now = new Date(verifierClock.millis());
    Date expirationTime = claimsSet.getExpirationTime();
    Date notBeforeTime = claimsSet.getNotBeforeTime();
    Date issueTime = claimsSet.getIssueTime();

    if (claimsSet.getIssuer() == null) {
      throw new AuthenticationServiceException("Issuer claim is required");
    }

    if (claimsSet.getExpirationTime() == null) {
      throw new AuthenticationServiceException("Expiration time claim required");
    }

    if (claimsSet.getIssueTime() == null) {
      throw new AuthenticationServiceException("Issue time claim required");
    }

    if (now.after(expirationTime)) {
      throw new AuthenticationServiceException("Expiration time claim expired: " + expirationTime);
    }

    if (!claimsSet.getIssuer().equals(getIssuer())) {
      throw new AuthenticationServiceException(String.format("Issuer claim is %s, requires %s ", claimsSet.getIssuer(), getIssuer()));
    }

    if (now.before(issueTime)) {
      throw new AuthenticationServiceException("Issue time claim in the future: " + issueTime);
    }

    if (claimsSet.getNotBeforeTime() != null) {
      if (now.before(claimsSet.getNotBeforeTime())) {
        throw new AuthenticationServiceException("Not before time claim precedes current time : " + notBeforeTime);
      }
    }

    return Auth0JWTAuthentication.create(claimsSet);

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
}
